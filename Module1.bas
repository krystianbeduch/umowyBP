Attribute VB_Name = "Module1"
Sub ShowDodajUsluge()
    DodajUslugeForm.Show
End Sub

Sub AddNewItem(itemName As String, itemPrice As Currency, itemQuantity As Integer, itemTotal As Currency, isPersons As Boolean)
    Set doc = ActiveDocument
    Dim rng As Range
    Set rng = Selection.Range

    ' Wstawienie nowego wiersza w miejscu, gdzie znajduje siê kursor
    rng.text = itemName & ": " & vbTab & vbTab
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z cen¹ jednostkow¹
    Dim txtPrice As ContentControl
    Set txtPrice = doc.ContentControls.Add(wdContentControlText, rng)
    txtPrice.Range.text = FormatCurrency(itemPrice)
    txtPrice.Title = itemName & "_cena_jednostkowa"
    
    ' Ustawienie zakresu za formantem tekstowym z cen¹
    rng.SetRange txtPrice.Range.End, txtPrice.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    rng.text = "  x  "
    rng.Collapse wdCollapseEnd
    
    ' Sprawdzenie czy wprowdzona ilosc to ilosc_osob
    If isPersons = True Then
        ' Wstawienie w³aœciwoœci dokumentu (DocProperty) z iloœci¹
        Dim fld As Field
        On Error Resume Next ' W³¹czenie obs³ugi b³êdów
        Set fld = ActiveDocument.Fields.Add(rng, wdFieldDocProperty, "ile_osob", False)
        On Error GoTo 0 ' Wy³¹czenie obs³ugi b³êdów
        
        If fld Is Nothing Then
            MsgBox "Nie udalo sie dodac pola DocProperty. Upewnij sie, ¿e wlasciwosc 'ile_osob' istnieje w dokumencie.", vbExclamation, "Error"
            Exit Sub
        End If
        fld.Update
    
        ' Ustawienie zakresu za polem DocProperty
        rng.SetRange fld.Result.End + 1, fld.Result.End + 1
        rng.Collapse wdCollapseEnd
    
        'rng.Collapse wdCollapseEnd
        rng.text = " osób = "
        rng.Collapse wdCollapseEnd
        
    Else
        ' Wstawienie zwyklej ilosc
        Dim txtQuantity As ContentControl
        Set txtQuantity = doc.ContentControls.Add(wdContentControlText, rng)
        txtQuantity.Range.text = itemQuantity
        txtQuantity.Title = itemName & "_ilosc"
        
        ' Ustawienie zakresu za formantem tekstowym z ca³kowitym kosztem
        rng.SetRange txtQuantity.Range.End, txtQuantity.Range.End
        rng.MoveEnd wdCharacter, 1
        rng.Collapse wdCollapseEnd
        
        rng.text = " ........ = "
        rng.Collapse wdCollapseEnd
    End If

    ' Wstawienie formantu tekstowego z ca³kowitym kosztem
    Dim txtTotal As ContentControl
    Set txtTotal = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotal.Range.text = FormatCurrency(itemTotal)
    txtTotal.Title = itemName & "_cena_calosciowa"
    
    ' Ustawienie zakresu za formantem tekstowym z ca³kowitym kosztem
    rng.SetRange txtTotal.Range.End, txtTotal.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie nowej linii
    rng.text = vbCrLf

    ' Aktualizacja kwoty razem ca³kowitej
    UpdateTotalAmount
End Sub

Sub UpdateTotalAmount()
    Set doc = ActiveDocument
    
    ' Deklaracja zmiennej do przechowywania ca³kowitej kwoty
    Dim totalAmount As Currency
    totalAmount = 0
    
    ' Deklaracja zmiennej do przechowywania formantów
    Dim cc As ContentControl
    
    ' Pêtla przez wszystkie formanty w dokumencie
    For Each cc In doc.ContentControls
        If Right(cc.Title, 16) = "_cena_calosciowa" Then
            totalAmount = totalAmount + CCur(cc.Range.text)
        End If
    Next cc
    
    Dim totalCC As ContentControl
    ' Próba znalezienia formantu z tytu³em "razem_kwota_brutto"
    On Error Resume Next
    Set totalCC = doc.SelectContentControlsByTitle("razem_kwota_brutto")(1)
    On Error GoTo 0
    
    ' Jeœli formant zosta³ znaleziony zaktualizuj kwote calkowita
    If Not totalCC Is Nothing Then
        totalCC.Range.text = FormatCurrency(totalAmount)
    End If
End Sub

Sub UpdateTotalPrice()
    Set doc = ActiveDocument
        
    ' Deklaracja zmiennej do przechowywania formantów
    Dim cc As ContentControl
    
    ' Deklaracja zmiennych do przechowywania informacji cenowych
    Dim itemPrice As Currency
    Dim itemQuantity As Integer
    Dim itemTotal As Currency
        
    ' Pêtla przez wszystkie formanty w dokumencie
    For Each cc In doc.ContentControls
        If Right(cc.Title, 17) = "_cena_jednostkowa" Then
            itemPrice = CCur(cc.Range.text)
            itemQuantity = GetDocProperty("ile_osob")
            
            ' Pozyskanie bazowego tytulu formantu
            Dim baseTitle As String
            baseTitle = Left(cc.Title, Len(cc.Title) - 17)
            
            Dim totalCC As ContentControl
            On Error Resume Next
            Set totalCC = doc.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
            On Error GoTo 0
            
            itemTotal = itemPrice * itemQuantity
            If Not totalCC Is Nothing Then
                totalCC.Range.text = FormatCurrency(itemTotal)
            End If
        End If
    Next cc
    
    ' Aktualizacja kwoty razem
    UpdateTotalAmount
End Sub

Sub AddNewItemOnlyPrice(itemName As String, itemPrice As Currency)
    Set doc = ActiveDocument
    Dim rng As Range
    Set rng = Selection.Range

    ' Wstawienie nowego wiersza w miejscu, gdzie znajduje sie kursor
    rng.text = itemName & ": " & vbTab & vbTab & vbTab & vbTab
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z cena calosciowa
    Dim txtTotal As ContentControl
    Set txtTotal = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotal.Range.text = FormatCurrency(itemPrice)
    txtTotal.Title = itemName & "_cena_calosciowa"
    
    ' Ustawienie zakresu za formantem tekstowym z calkowitym kosztem
    rng.SetRange txtTotal.Range.End, txtTotal.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie nowej linii
    rng.text = vbCrLf

    ' Aktualizacja kwoty razem calkowitej
    UpdateTotalAmount
End Sub
'End Sub

Sub InsertTextToContentControl(controlName As String, text As String)
    Dim cc As ContentControl
    
    ' Znajdz formant zawartosci i wstaw tekst
    For Each cc In ActiveDocument.ContentControls
        If cc.Title = controlName Then
            cc.Range.text = text
            Exit Sub
        End If
    Next cc
End Sub

Function GetDocProperty(propName As String) As Variant
    On Error Resume Next
    GetDocProperty = ActiveDocument.CustomDocumentProperties(propName).value
    On Error GoTo 0
End Function

Sub UpdateTransportDate(ByVal ContentControl As ContentControl, ByVal transportTag As String)
    ' Znajdz formant "termin_od_transport" i skopiuj datê
    Dim cc As ContentControl
    For Each cc In ThisDocument.ContentControls
        If cc.Tag = transportTag Then
            cc.Range.text = ContentControl.Range.text
            Exit For
        End If
    Next cc
End Sub

Sub UpdateTotalPriceInline(ByVal ContentControl As ContentControl)
    Dim itemQuantity As Integer
        
    ' Pobierz cene jednostkowa
    Dim itemPrice As Currency
    itemPrice = CCur(ContentControl.Range.text)
    
    ' Pobierz tytul formantu
    Dim baseTitle As String
    baseTitle = Left(ContentControl.Title, Len(ContentControl.Title) - 17)
    
    ' Pobierz formant z cena calkowita na podstawie tytulu
    Dim totalCC As ContentControl
    On Error Resume Next
    Set totalCC = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
    On Error GoTo 0
    
    ' Sprawdz, czy wiersz zawiera docProperty 'ile_osob'
    If Not totalCC Is Nothing Then
        Dim fld As Field
        Dim isDocPropertyPresent As Boolean
        isDocPropertyPresent = False

        For Each fld In ActiveDocument.Fields
            If fld.Type = wdFieldDocProperty And fld.Code.text Like "*ile_osob*" Then
                If fld.Result.Start >= ContentControl.Range.End And fld.Result.End <= totalCC.Range.Start Then
                    isDocPropertyPresent = True
                    Exit For
                End If
            End If
        Next fld

        If isDocPropertyPresent Then ' zawiera docProperty 'ile_osob'
            ' Obliczenie nowej ca³kowitej ceny
            Dim itemTotal As Currency
            itemQuantity = GetDocProperty("ile_osob")
            itemTotal = itemPrice * itemQuantity

            ' Aktualizacja formantu z cen¹ ca³kowit¹
            totalCC.Range.text = FormatCurrency(itemTotal)
        Else ' Nie zawiera DocProperty 'ile_osob', sprawdŸ formant z iloœci¹
            On Error Resume Next
            Dim ccQuantity As ContentControl
            Set ccQuantity = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_ilosc")(1)
            On Error GoTo 0

            If Not ccQuantity Is Nothing Then
                itemQuantity = CInt(ccQuantity.Range.text)
                itemTotal = itemPrice * itemQuantity
    
                ' Aktualizacja formantu z cen¹ ca³kowit¹
                If Not totalCC Is Nothing Then
                    totalCC.Range.text = FormatCurrency(itemTotal)
                End If
            Else
                MsgBox "Brak formantu z iloacia dla tytulu: " & baseTitle, vbExclamation, "Brak danych"
            End If
        End If
    End If

    ' Aktualizacja sumy ca³kowitej
    UpdateTotalAmount
    
End Sub

Sub UpdateTotalPriceInlineForQuantity(ByVal ContentControl As ContentControl)
    Dim itemQuantity As Integer
    itemQuantity = CInt(ContentControl.Range.text)
    
    ' Pobierz tytul formantu
    Dim baseTitle As String
    baseTitle = Left(ContentControl.Title, Len(ContentControl.Title) - 6)
        
    ' Pobierz formant z cena jednostkowa na podstawie tytulu
    Dim ccPrice As ContentControl
    On Error Resume Next
    Set ccPrice = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_cena_jednostkowa")(1)
    On Error GoTo 0
    
    If Not ccPrice Is Nothing Then
        ' Pobierz cene jednostkowa
        Dim itemPrice As Currency
        itemPrice = CCur(ccPrice.Range.text)
        
        ' Pobierz formant z cena calkowita na podstawie tytulu
        Dim totalCC As ContentControl
        On Error Resume Next
        Set totalCC = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
        On Error GoTo 0
    
        ' Obliczenie nowej calkowitej ceny
        Dim itemTotal As Currency
        itemTotal = itemPrice * itemQuantity
        
        ' Aktualizacja formantu z cena calkowita
        If Not totalCC Is Nothing Then
            totalCC.Range.text = FormatCurrency(itemTotal)
        End If
    Else
        MsgBox "Brak formantu z cena jednostkowa dla tytulu: " & baseTitle, vbExclamation, "Brak danych"
    End If

    ' Aktualizacja sumy ca³kowitej
    UpdateTotalAmount
End Sub

'Function GetBookmarkValueAsInteger(bookmarkName As String) As Long
    'On Error Resume Next ' Ignoruj bledy, jesli zakladka nie istnieje
    'Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
    'If Not bookmarkRange Is Nothing Then
        'GetBookmarkValueAsInteger = CLng(bookmarkRange.text) ' return
    'Else
        'MsgBox "Zakladka '" & bookmarkName & "' nie istnieje.", vbExclamation, "Error"
        'GetBookmarkValueAsInteger = 0 ' return
    ''End If
    'On Error GoTo 0
'End Function

'Sub UpdateBookmarkOfPrice(bookmarkName As String, value As Variant)
 '   On Error Resume Next ' Ignoruj bledy, jesli zakladka nie istnieje
  '  Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
   ' If Not bookmarkRange Is Nothing Then
    '    bookmarkRange.text = CStr(value)
        ' Dodaj zakladke na nowo, aby zachowac ja po zmianie tekstu
     '   doc.Bookmarks.Add bookmarkName, bookmarkRange
    'Else
     '   MsgBox "Zakladka '" & bookmarkName & "' nie istnieje.", vbExclamation, "Error"
    'End If
    'On Error GoTo 0
'End Sub

Sub AddPickupAndDropOffLocations()
    ' Inicjalizacja polaczenia z baza danych
    InitializeConnection
    
    ' Zapytanie SQL do pobrania danych
    Dim strSQL As String
    strSQL = "SELECT miejsce_odbioru, miejscowosc, ulica, " & _
                "numer, miejscowosc_miejsca_odbioru, ulica_miejsca_odbioru, " & _
                "numer_miejsca_odbioru, kod_pocztowy_miejsca_odbioru " & _
                "FROM klienci WHERE id = " & zamawiajacy.id & ";"

    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(strSQL)
    
    ' SprawdŸ wynik i utwórz tekst do wstawienia do formantów
    If Not rs.EOF Then
        If IsNull(rs.Fields("miejsce_odbioru").value) Then
            ' Jeœli "miejsce_odbioru" jest NULL - jest takie samo jak adres
            With zamawiajacy.miejsce_odbioru
                .miejsce_odbioru = ""
                .miejscowosc = zamawiajacy.miejscowosc
                .ulica = zamawiajacy.ulica
                .numer = zamawiajacy.numer
                .kod_pocztowy = zamawiajacy.kod_pocztowy
            End With
        
            'resultText = rs.Fields("miejscowosc").value & ", " & rs.Fields("ulica").value & " " & rs.Fields("numer").value
        Else
            ' Jeœli "miejsce_odbioru" nie jest NULL - miejsce odbioru jest ustalone w innym miejscu
            With zamawiajacy.miejsce_odbioru
                .miejsce_odbioru = rs.Fields("miejsce_odbioru").value
                .miejscowosc = rs.Fields("miejscowosc_miejsca_odbioru").value
                .ulica = rs.Fields("ulica_miejsca_odbioru").value
                .numer = rs.Fields("numer_miejsca_odbioru").value
                .kod_pocztowy = rs.Fields("kod_pocztowy_miejsca_odbioru").value
            End With
             
            
            'resultText = rs.Fields("miejsce_odbioru").value & " " & rs.Fields("miejscowosc_miejsca_odbioru").value & ", " & rs.Fields("ulica_miejsca_odbioru").value & " " & rs.Fields("numer_miejsca_odbioru").value
        End If
    Else
        MsgBox "Nie znaleziono wyników dla zapytania.", vbExclamation, "Error"
    End If
    
    ' Zamkniêcie po³¹czenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
End Sub

Sub UpdateTodayDate()
    ' Pobranie dzisiejszej daty
    Dim todayDate As Date
    todayDate = Date

    ' Pobranie formantu datowego "data_dzis"
    Dim cc As ContentControl
    On Error Resume Next
    Set cc = ActiveDocument.SelectContentControlsByTitle("data_dzis")(1)
    On Error GoTo 0
    
    ' Aktualizacja daty w formancie, jeœli istnieje
    If Not cc Is Nothing Then
        cc.Range.text = Format(todayDate, "dd.mm.yyyy")
    Else
        MsgBox "Nie znaleziono formantu 'data_dzis'.", vbExclamation, "B³¹d"
    End If
End Sub
