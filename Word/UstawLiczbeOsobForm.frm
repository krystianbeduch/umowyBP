VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} UstawLiczbeOsobForm 
   Caption         =   "Ustaw liczbê osób"
   ClientHeight    =   1935
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5310
   OleObjectBlob   =   "UstawLiczbeOsobForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "UstawLiczbeOsobForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub cboConfirm_Click()
    Dim strNumberOfPeople As String
    Dim intNumberOfPeople As Integer
    
    ' Pobierz wprowadzona liczbe osob z TextBoxa
    strNumberOfPeople = Trim(Me.txtNumberOfPeople.value)
    
    ' Sprawdz czy wprowadzona wartosc nie jest pusta
    If strNumberOfPeople = "" Then
        MsgBox "Wprowadz liczbe osob.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Sprobuj przekonwertowac wprowadzony tekst na liczbe calkowita
    On Error Resume Next
    intNumberOfPeople = CInt(strNumberOfPeople)
    If Err.number <> 0 Then
        MsgBox "Blad konwersji.", vbExclamation, "Error"
        Err.Clear
        Exit Sub
    End If
    On Error GoTo 0
    
    ' Sprawdz czy wprowadzona liczba calkowita jest liczba dodatnia
    If intNumberOfPeople <= 0 Then
        MsgBox "Wprowadzona wartosc nie jest liczba calowita dodatnia.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Zaktualizuj docProperty 'ile_osob'
    UpdateCustomProperty ActiveDocument, DOC_PROP_NUMBER_OF_PEOPLE, intNumberOfPeople
    ActiveDocument.Fields.Update
        
    ' Zaktualizuj ceny po zmianie liczby osob
    UpdateTotalPricesAfterChangingNumberOfPeople
    
    ' Ustaw minimalna liczbe osob
    SetMinNumberOfPeople
    
    ' Ustaw opiekunow gratis
    SetCareeFree
    
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me
End Sub

Private Sub UpdateCustomProperty(doc As Document, propertyName As String, propertyValue As Variant)
    On Error Resume Next
    ' Jesli wlasciwosc istnieje, zaktualizuj ja; w przeciwnym razie dodaj nowa wlasciwosc
    If doc.CustomDocumentProperties(propertyName).Exists Then
        doc.CustomDocumentProperties(propertyName).value = propertyValue
    Else
        doc.CustomDocumentProperties.Add Name:=propertyName, LinkToContent:=False, _
            Type:=msoPropertyTypeString, value:=propertyValue
    End If
    On Error GoTo 0
End Sub

Private Sub UpdateTotalPricesAfterChangingNumberOfPeople()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

    Set doc = ActiveDocument
        
    ' Deklaracja zmiennej do przechowywania formantow
    Dim cc As ContentControl
    
    ' Deklaracja zmiennych do przechowywania informacji cenowych
    Dim itemPrice As Currency
    Dim itemQuantity As Integer
    Dim itemTotal As Currency
        
    ' Petla przez wszystkie formanty w dokumencie
    For Each cc In doc.ContentControls
        If Right(cc.Title, 17) = "_cena_jednostkowa" Then
            Debug.Print "Title: " & cc.Title
            Debug.Print "Text: " & cc.Range.text
            Debug.Print "Start Position: " & cc.Range.Start
            Debug.Print "End Position: " & cc.Range.End
        
            itemPrice = CCur(cc.Range.text)
            itemQuantity = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
            
            ' Pozyskanie bazowego tytulu formantu
            Dim baseTitle As String
            baseTitle = Left(cc.Title, Len(cc.Title) - 17)
            
            ' Znalezienie i aktualizacja formantu z calkowitym kosztem
            Dim totalCC As ContentControl
            On Error Resume Next
            Set totalCC = doc.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
            On Error GoTo ErrorHandler
            
            itemTotal = itemPrice * itemQuantity
            If Not totalCC Is Nothing Then
                totalCC.Range.text = FormatCurrency(itemTotal)
            End If
        End If
    Next cc
    
    ' Aktualizacja kwoty razem
    UpdateTotalAmount
    
    ' Zmiana wartosci w sekcji "dodatkowo_platne"
    For Each cc In doc.ContentControls
        If cc.Title = "dodatkowo_platne_cj" Then
            ' Pozyskanie tekstu ceny jednostkowej
            Dim priceText As String
            priceText = cc.Range.text
            
            ' Wyodrebnienie wartosci liczbowej z tekstu
            Dim priceWithoutCurrency As Currency
            priceWithoutCurrency = ExtractNumber(priceText)
            
            ' Pobranie symbolu waluty z tekstu
            currencySymbol = ExtractCurrencySymbol(priceText)
            
            ' Obliczenie nowej wartosci calkowitej
            Dim updatedTotal As Currency
            updatedTotal = priceWithoutCurrency * itemQuantity
            
            ' Znalezienie odpowiednigo formantu, aktualizacja tekstu
            On Error Resume Next
            Set totalCC = doc.SelectContentControlsByTitle("dodatkowo_platne_cc")(1)
            On Error GoTo ErrorHandler
            If Not totalCC Is Nothing Then
                totalCC.Range.text = Format(updatedTotal, "#,##0.00") & " " & currencySymbol
            End If
            Exit For
        End If
    Next cc
    
    ' Aktualizacja reprezentacji tekstowej w sekcji "dodatkowo_platne"
    On Error Resume Next
    Set totalCC = doc.SelectContentControlsByTitle("dodatkowo_platne_slownie")(1)
    On Error GoTo ErrorHandler
    If Not totalCC Is Nothing Then
        UpdateCurrencyToText totalCC, updatedTotal, currencySymbol
    End If
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

' Funkcja wyodrebnia wartosc liczbowa z tekstu, ignorujac znaki waluty
Private Function ExtractNumber(text As String) As Currency
    Dim num As String
    Dim i As Integer
    
    num = ""
    ' Petla przez wszystkie znaki w tekscie
    For i = 1 To Len(text)
        ' Dodanie do wyniku tylko cyfr, przecinka i kropki
        If Mid(text, i, 1) Like "[0-9.,]" Then
            num = num & Mid(text, i, 1)
        End If
    Next i
    
    ExtractNumber = CCur(num)
End Function

' Funkcja wyodrebnia symbol waluty z tekstu
Private Function ExtractCurrencySymbol(text As String) As String
    Dim i As Integer
    Dim j As Integer
    Dim symbol As String
    Dim symbols() As String
    
    Dim extr As String
    ' Lista obs³ugiwanych symboli walut
    symbols = Split("z³,€,Kè", ",")
    
    ' Petla przez wszystkie znaki w tekscie
    For i = 1 To Len(text)
        ' Sprawdzenie czy znak jest symbolem waluty
        For j = LBound(symbols) To UBound(symbols)
            symbol = symbols(j)
            extr = Mid(text, i, Len(symbol))
            If extr = symbol Then
                ExtractCurrencySymbol = symbol
                Exit Function
            End If
        Next j
    Next i
    
    ' Zwrocenie pustego ciagu, jesli symbol nie zostal znaleziony
    ExtractCurrencySymbol = ""
End Function

