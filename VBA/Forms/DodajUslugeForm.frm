VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} DodajUslugeForm 
   Caption         =   "Dodaj us³ugê"
   ClientHeight    =   3360
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7800
   OleObjectBlob   =   "DodajUslugeForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "DodajUslugeForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Private Sub UserForm_Initialize()
    On Error GoTo ErrorHandler ' Wlaczenie obslugi bledow
    ' Ustawienie checkboxa jako zaznaczonego
    chkIsPersons.value = True
    Dim numberOfPersons As Integer
    numberOfPersons = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
    ' Ustawienie wartoœci w txtQuantity i zablokowanie pola
    txtQuantity.text = numberOfPersons
    txtQuantity.Enabled = False
    
    ' Ustaw fokus na odpowiednio text boxa
    txtName.SetFocus
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Private Sub btnAddItem_Click()
    On Error GoTo ErrorHandler ' Wlaczenie obslugi bledow

    Dim serviceName As String ' Nazwa uslugi
    Dim unitPrice As Currency ' Cena jednostkowa
        
    ' Pobranie wartosci z pol tekstowych + obsluga bledow
    serviceName = Trim(txtName.text)
    
    If serviceName = "" Then
        MsgBox "Prosze podac nazwe uslugi", vbExclamation, "Error"
        Exit Sub
    End If
    
    On Error GoTo ConversionError
    unitPrice = CCur(txtUnitPrice.text)
    On Error GoTo ErrorHandler
    
    Dim currencySymbol As String ' Symbol waluty
    Dim finalPrice As Currency ' Cena koncowa po przeliczeniu (przez ilosc)
    Dim exchangeRate As Currency ' Kurs wymiany waluty
    
    ' Sprawdzenie wybranej waluty
    If rbtnPLN.value = True Then
        finalPrice = unitPrice
        currencySymbol = "z³"
    ElseIf rbtnEUR.value = True Or rbtnCZK.value = True Then
        On Error GoTo ConversionError
        exchangeRate = CCur(txtExchangeRate.text)
        On Error GoTo ErrorHandler
        ' Zaokraglenie gorne do wartosci calkowitej
        finalPrice = Ceiling(unitPrice * exchangeRate)
        If rbtnEUR.value = True Then
            currencySymbol = "€"
        ElseIf rbtnCZK.value = True Then
            currencySymbol = "Kè"
        End If
        serviceName = serviceName & " (" & unitPrice & " " & currencySymbol & ")"
    End If
    
    ' Czy podana cena jest cena calosciowa
    If chkTotalPrice.value = True Then
        If rbtnEUR.value = True Or rbtnCZK.value = True Then
            ' Podana cena jest cena w innej walucie (inne formatowanie tekstu)
            AddNewServiceOnlyPrice serviceName, finalPrice
        Else
            AddNewServiceOnlyPrice serviceName, unitPrice
        End If
    Else ' podana cena jest cena na osobe (czyli mnozymy ja przez ilosc osob)
        Dim itemQuantity As Integer ' ilosc (osob)
        Dim totalPrice As Currency
        On Error GoTo ConversionError
        itemQuantity = CInt(txtQuantity.text)
        On Error GoTo ErrorHandler
        
        If rbtnEUR.value = True Or rbtnCZK.value = True Then
            ' Podana cena jest cena w innej walucie (inne formatowanie tekstu)
            totalPrice = finalPrice * itemQuantity
            AddNewService serviceName, finalPrice, itemQuantity, _
            totalPrice, chkIsPersons.value
        Else
            ' Dodanie nowego wiersza do dokumentu
            totalPrice = unitPrice * itemQuantity
            AddNewService serviceName, unitPrice, itemQuantity, _
            totalPrice, chkIsPersons.value
        End If
    End If
 
    ' Wyczysc pola tekstowe
    txtName.text = ""
    txtUnitPrice.text = ""
    txtExchangeRate.text = ""
    txtQuantity.text = ""
    
    ' Zamknij formularz
    Unload Me
    Exit Sub
    
ConversionError:
    MsgBox "Wprowadzono wartosc nie jest liczba. " & _
    "Prosze wprowadzic poprawna liczbe", vbExclamation, "Error"
    Exit Sub

ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
    Exit Sub
End Sub

Private Sub chkTotalPrice_Click()
    If chkTotalPrice.value = True Then
        txtQuantity.Visible = False
        chkIsPersons.Visible = False
    Else
        txtQuantity.Visible = True
        chkIsPersons.Visible = True
    End If
End Sub

Private Sub chkIsPersons_Click()
    If chkIsPersons.value = True Then
        ' Pobierz wartosc wlasciwosci dokumentu
        Dim numberOfPersons As String
        numberOfPersons = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
        
        ' Ustaw wartosc w txtQuantity i zablokuj pole
        txtQuantity.text = numberOfPersons
        txtQuantity.Enabled = False
    Else
        ' Odblokuj pole txtQuantity
        txtQuantity.Enabled = True
        txtQuantity.text = ""
    End If
End Sub

Private Sub rbtnPLN_Click()
    txtExchangeRate.Visible = False
    lblExchangeRate.Visible = False
End Sub

Private Sub rbtnEUR_Click()
    txtExchangeRate.Visible = True
    txtExchangeRate.text = ""
    txtExchangeRate.SetFocus
    lblExchangeRate.Visible = True
End Sub

Private Sub rbtnCZK_Click()
    rbtnEUR_Click
End Sub
