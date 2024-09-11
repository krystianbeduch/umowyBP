Attribute VB_Name = "Services"
Option Explicit

Sub AddNewService( _
    serviceName As String, _
    unitPrice As Currency, _
    itemQuantity As Integer, _
    totalPrice As Currency, _
    isPersons As Boolean)
    
    Set doc = ActiveDocument
    Dim rng As Range
    Set rng = Selection.Range
    
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

    ' Wstawienie nowego wiersza w miejscu, gdzie znajduje sie kursor
    rng.text = serviceName & ": " & vbTab & vbTab
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z cena jednostkowa
    Dim txtPrice As ContentControl
    Set txtPrice = doc.ContentControls.Add(wdContentControlText, rng)
    txtPrice.Range.text = FormatCurrency(unitPrice)
    txtPrice.Title = serviceName & "_cena_jednostkowa"
    
    ' Ustawienie zakresu za formantem tekstowym z cena
    rng.SetRange txtPrice.Range.End, txtPrice.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    rng.text = "  x  "
    rng.Collapse wdCollapseEnd
    
    ' Sprawdzenie czy wprowdzona ilosc to ilosc_osob
    If isPersons = True Then
        ' Wstawienie wlasciwosci dokumentu (DocProperty) z iloscia
        Dim fld As Field
        On Error Resume Next ' Wlaczenie obslugi bledow
        Set fld = ActiveDocument.Fields.Add(rng, wdFieldDocProperty, DOC_PROP_NUMBER_OF_PEOPLE, False)
        On Error GoTo ErrorHandler
        
        If fld Is Nothing Then
            MsgBox "Nie udalo sie dodac pola DocProperty. " & _
            "Upewnij sie, ¿e wlasciwosc '" & DOC_PROP_NUMBER_OF_PEOPLE & "' istnieje w dokumencie", _
            vbExclamation, "Error"
            Exit Sub
        End If
        fld.Update
    
        ' Ustawienie zakresu za polem DocProperty
        rng.SetRange fld.result.End + 1, fld.result.End + 1
        rng.Collapse wdCollapseEnd
    
        rng.text = " osób = "
        rng.Collapse wdCollapseEnd
        
    Else
        ' Wstawienie zwyklej ilosc
        Dim txtQuantity As ContentControl
        Set txtQuantity = doc.ContentControls.Add(wdContentControlText, rng)
        txtQuantity.Range.text = itemQuantity
        txtQuantity.Title = serviceName & "_ilosc"
        
        ' Ustawienie zakresu za formantem tekstowym z calkowitym kosztem
        rng.SetRange txtQuantity.Range.End, txtQuantity.Range.End
        rng.MoveEnd wdCharacter, 1
        rng.Collapse wdCollapseEnd
        
        rng.text = " ........ = "
        rng.Collapse wdCollapseEnd
    End If

    ' Wstawienie formantu tekstowego z calkowitym kosztem
    Dim txtTotal As ContentControl
    Set txtTotal = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotal.Range.text = FormatCurrency(totalPrice)
    txtTotal.Title = serviceName & "_cena_calosciowa"
    
    ' Ustawienie zakresu za formantem tekstowym z calkowitym kosztem
    rng.SetRange txtTotal.Range.End, txtTotal.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie nowej linii
    rng.text = vbCrLf

    ' Aktualizacja kwoty razem calkowitej
    UpdateTotalAmount
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub AddNewServiceOnlyPrice(serviceName As String, unitPrice As Currency)
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

    Set doc = ActiveDocument
    Dim rng As Range
    Set rng = Selection.Range

    ' Wstawienie nowego wiersza w miejscu, gdzie znajduje sie kursor
    rng.text = serviceName & ": " & vbTab & vbTab & vbTab & vbTab
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z cena calosciowa, ustawienie tytulu
    Dim txtTotal As ContentControl
    Set txtTotal = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotal.Range.text = FormatCurrency(unitPrice)
    txtTotal.Title = serviceName & "_cena_calosciowa"
    
    ' Ustawienie zakresu za formantem tekstowym z calkowitym kosztem
    rng.SetRange txtTotal.Range.End, txtTotal.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie nowej linii
    rng.text = vbCrLf

    ' Aktualizacja kwoty razem calkowitej
    UpdateTotalAmount
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub UpdateTotalAmount()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

    Set doc = ActiveDocument
    Dim totalAmount As Currency
    totalAmount = 0
    
    ' Deklaracja zmiennej do przechowywania formantow
    Dim cc As ContentControl
    
    ' Petla przez wszystkie formanty w dokumencie
    For Each cc In doc.ContentControls
        If Right(cc.Title, 16) = "_cena_calosciowa" Then
            ' Sprawdzenie czy wartosc jest liczba
            If IsNumeric(cc.Range.text) Then
                totalAmount = totalAmount + CCur(cc.Range.text)
            Else
                MsgBox "Wartosc w formancie '" & cc.Title & "' nie jest prawid³ow¹ liczb¹.", _
                vbExclamation, "Error"
            End If
        End If
    Next cc
    
    ' Aktualizacja formantu 'razem_kwota_brutto'
    Call InsertTextToContentControl("razem_kwota_brutto", FormatCurrency(totalAmount))
    
    ' Proba znalezienia formantu z tytulem "razem_kwota_brutto_slownie"
    On Error Resume Next
    Dim totalCC As ContentControl
    Set totalCC = doc.SelectContentControlsByTitle("razem_kwota_brutto_slownie")(1)
    On Error GoTo ErrorHandler
    
    ' Jesli formant zosta³ znaleziony zaktualizuj kwote calkowita
    If Not totalCC Is Nothing Then
        ' Aktualizacja reprezentacji slownej
        UpdateCurrencyToText totalCC, totalAmount, ""
    Else
        MsgBox "Nie znaleziono formantu o tytule 'razem_kwota_brutto_slownie'.", _
        vbExclamation, "B³¹d"
    End If
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub UpdateTotalPriceInline(ByVal ContentControl As ContentControl)
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

    Dim itemQuantity As Integer
        
    ' Pobierz cene jednostkowa
    Dim unitPrice As Currency
    unitPrice = CCur(ContentControl.Range.text)
    
    ' Pobierz tytul formantu
    Dim baseTitle As String
    baseTitle = Left(ContentControl.Title, Len(ContentControl.Title) - 17)
    
    ' Pobierz formant z cena calkowita na podstawie tytulu
    Dim totalCC As ContentControl
    On Error Resume Next
    Set totalCC = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
    On Error GoTo ErrorHandler
    
    ' Sprawdz, czy wiersz zawiera docProperty 'ile_osob'
    If Not totalCC Is Nothing Then
        Dim fld As Field
        Dim isDocPropertyPresent As Boolean
        isDocPropertyPresent = False

        For Each fld In ActiveDocument.Fields
            If fld.Type = wdFieldDocProperty And fld.Code.text Like "*" & DOC_PROP_NUMBER_OF_PEOPLE & "*" Then
                If fld.result.Start >= ContentControl.Range.End And fld.result.End <= totalCC.Range.Start Then
                    isDocPropertyPresent = True
                    Exit For
                End If
            End If
        Next fld

        If isDocPropertyPresent Then ' zawiera docProperty 'ile_osob'
            ' Obliczenie nowej calkowitej ceny
            Dim totalPrice As Currency
            itemQuantity = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
            totalPrice = unitPrice * itemQuantity

            ' Aktualizacja formantu z cena calkowita
            totalCC.Range.text = FormatCurrency(totalPrice)
        Else ' Nie zawiera DocProperty 'ile_osob', sprawdz formant z iloscia
            On Error Resume Next
            Dim ccQuantity As ContentControl
            Set ccQuantity = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_ilosc")(1)
            On Error GoTo ErrorHandler

            If Not ccQuantity Is Nothing Then
                itemQuantity = CInt(ccQuantity.Range.text)
                totalPrice = unitPrice * itemQuantity
    
                ' Aktualizacja formantu z cena calkowita
                If Not totalCC Is Nothing Then
                    totalCC.Range.text = FormatCurrency(totalPrice)
                End If
            Else
                MsgBox "Brak formantu z iloacia dla tytulu: " & baseTitle, vbExclamation, "Brak danych"
            End If
        End If
    End If

    ' Aktualizacja sumy ca³kowitej
    UpdateTotalAmount
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub UpdateTotalPriceInlineForQuantity(ByVal ContentControl As ContentControl)
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow

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
        Dim unitPrice As Currency
        unitPrice = CCur(ccPrice.Range.text)
        
        ' Pobierz formant z cena calkowita na podstawie tytulu
        Dim totalCC As ContentControl
        On Error Resume Next
        Set totalCC = ActiveDocument.SelectContentControlsByTitle(baseTitle & "_cena_calosciowa")(1)
        On Error GoTo 0
    
        ' Obliczenie nowej calkowitej ceny
        Dim totalPrice As Currency
        totalPrice = unitPrice * itemQuantity
        
        ' Aktualizacja formantu z cena calkowita
        If Not totalCC Is Nothing Then
            totalCC.Range.text = FormatCurrency(totalPrice)
        End If
    Else
        MsgBox "Brak formantu z cena jednostkowa dla tytulu: " & baseTitle, vbExclamation, "Brak danych"
    End If

    ' Aktualizacja sumy calkowitej
    UpdateTotalAmount
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub AddExtraPayableDuringTheTrip( _
    ByVal unitPrice As Currency, _
    ByVal itemQuantity As Integer, _
    ByVal totalPrice As Currency, _
    ByVal currencySymbol As String)
    
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    Set doc = ActiveDocument
    Dim rng As Range
    Set rng = Selection.Range

    ' Wstawienie nowego wiersza w miejscu, gdzie znajduje siê kursor
    rng.text = "oraz dodatkowo p³atne w czasie wycieczki: "
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z cena jednostkowa
    Dim txtPrice As ContentControl
    Set txtPrice = doc.ContentControls.Add(wdContentControlText, rng)
    txtPrice.Range.text = Format(unitPrice, "#,##0.00") & " " & currencySymbol
    txtPrice.Title = "dodatkowo_platne_cj"
    
    ' Ustawienie zakresu za formantem tekstowym z cena
    rng.SetRange txtPrice.Range.End, txtPrice.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    rng.text = "  x  "
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie wlasciwosci dokumentu (DocProperty) z iloscia
    Dim fld As Field
    On Error Resume Next ' Wlaczenie obslugi bledow
    Set fld = ActiveDocument.Fields.Add(rng, wdFieldDocProperty, DOC_PROP_NUMBER_OF_PEOPLE, False)
    On Error GoTo ErrorHandler
        
    If fld Is Nothing Then
        MsgBox "Nie udalo sie dodac pola DocProperty." & _
        "Upewnij sie, ¿e wlasciwosc '" & DOC_PROP_NUMBER_OF_PEOPLE & "' istnieje w dokumencie", _
        vbExclamation, "Error"
        Exit Sub
    End If
    fld.Update
    
    ' Ustawienie zakresu za polem DocProperty
    rng.SetRange fld.result.End + 1, fld.result.End + 1
    rng.Collapse wdCollapseEnd
    
    rng.text = " osób = "
    rng.Collapse wdCollapseEnd

    ' Wstawienie formantu tekstowego z calkowitym kosztem
    Dim txtTotal As ContentControl
    Set txtTotal = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotal.Range.text = Format(totalPrice, "#,##0.00") & " " & currencySymbol
    txtTotal.Title = "dodatkowo_platne_cc"
    
    ' Ustawienie zakresu za formantem tekstowym z calkowitym kosztem
    rng.SetRange txtTotal.Range.End, txtTotal.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    rng.text = " brutto ("
    rng.Collapse wdCollapseEnd
    
    ' Wstawienie formantu tekstowego na zapis slowny
    Dim txtTotalInWords As ContentControl
    Set txtTotalInWords = doc.ContentControls.Add(wdContentControlText, rng)
    txtTotalInWords.Title = "dodatkowo_platne_slownie"
    UpdateCurrencyToText txtTotalInWords, totalPrice, currencySymbol
    
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub UpdateCurrencyToText( _
    ByVal ContentControl As ContentControl, _
    ByVal totalAmount As Currency, _
    ByVal currencySymbol As String)
    
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    ' Sprawdzenie, czy przekazany ContentControl jest poprawny
    If ContentControl Is Nothing Then
        MsgBox "Przekazany ContentControl jest pusty.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Funkcja liczbe na tekst
    Dim textRepresentation As String
    textRepresentation = NumberToText(totalAmount)
    
    Dim currencyRepresentation As String
    Select Case currencySymbol
        Case "z³"
            currencyRepresentation = "z³otych"
        Case "€"
            currencyRepresentation = "euro"
        Case "Kè"
            currencyRepresentation = "koron czeskich"
        Case Else
            ' Domyslna waluta, jesli nie pasuje do zadnej z wymienionych
            currencyRepresentation = currencySymbol
    End Select
    
    ' Aktualizacja tekstu w formancie
    ContentControl.Range.text = textRepresentation & " " & currencyRepresentation & ")"
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub
