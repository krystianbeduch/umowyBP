VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ThisDocument"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Sub Wybierz_Wycieczke()
    Dim form As WyborWycieczkiZListyForm
    Set form = New WyborWycieczkiZListyForm
    form.Show ' Wyswietlenie UserForma
    Unload form ' Zwolnienie zasobow po zakonczeniu
End Sub

Sub Wprowadz_Ilosc_Osob()
    Dim form As WprowadzLiczbeOsobForm
    Set form = New WprowadzLiczbeOsobForm
    form.Show ' Wyswietlenie UserForma
    Unload form ' Zwolnienie zasobow po zakonczeniu
End Sub

Sub Wylicz_Kwoty()
    Set doc = ThisDocument
    
    ' Pobierz ilosc osob z zakladki
    Dim numberOfPeople As Long
    numberOfPeople = GetBookmarkValueAsInteger("ile_osob")
    
    ' Pobierz wartosci z zakladek i przelicz
    Dim basePrice As Long
    Dim entryTickets As Long
    basePrice = GetBookmarkValueAsInteger("cena_bazowa")
    entryTickets = GetBookmarkValueAsInteger("bilety_wstepu")
    
    Dim basePriceTotal As Long
    Dim entryTicketsTotal As Long
    Dim totalAmount As Long
    basePriceTotal = basePrice * numberOfPeople
    entryTicketsTotal = entryTickets * numberOfPeople
    totalAmount = basePriceTotal + entryTicketsTotal
    
    ' Zaktualizuj zakladki z przeliczonymi wartosciami
    UpdateBookmarkOfPrice "cena_bazowa_suma", basePriceTotal
    UpdateBookmarkOfPrice "bilety_wstepu_suma", entryTicketsTotal
    UpdateBookmarkOfPrice "razem_kwota_brutto", totalAmount
End Sub

Sub Wprowadz_zamawiajacego()
    Dim form As DodajZamawiajacegoForm
    Set form = New DodajZamawiajacegoForm
    form.Show ' Wyswietlenie UserForma
    Unload form ' Zwolnienie zasobow po zakonczeniu
    
    ' Wstaw dane do formantu "zamawiajacy"
    Dim resultText As String
    resultText = zamawiajacy.nazwa_instytucji & ", " & zamawiajacy.ulica & " " & zamawiajacy.numer & _
             ", " & zamawiajacy.kod_pocztowy & " " & zamawiajacy.miejscowosc
    Call InsertTextToContentControl("zamawiajacy", resultText)
    
    ' Wstaw dane do formantu "zamawiajacy_osoba"
    resultText = zamawiajacy.imie & " " & zamawiajacy.nazwisko
    Call InsertTextToContentControl("zamawiajacy_osoba", resultText)
    
    ' Wstaw dane do formantu "miejsce_odjazdu" i "miejsce_powrotu"
    AddPickupAndDropOffLocations
    resultText = zamawiajacy.miejsce_odbioru.miejscowosc & ", " & _
                 zamawiajacy.miejsce_odbioru.ulica & " " & zamawiajacy.miejsce_odbioru.numer & _
                 ", " & zamawiajacy.miejsce_odbioru.kod_pocztowy
    Call InsertTextToContentControl("miejsce_odjazdu", resultText)
    Call InsertTextToContentControl("miejsce_powrotu", resultText)
    
End Sub

Private Function GetBookmarkValueAsInteger(bookmarkName As String) As Long
    On Error Resume Next ' Ignoruj bledy, jesli zakladka nie istnieje
    Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
    If Not bookmarkRange Is Nothing Then
        GetBookmarkValueAsInteger = CLng(bookmarkRange.text) ' return
    Else
        MsgBox "Zakladka '" & bookmarkName & "' nie istnieje.", vbExclamation, "Error"
        GetBookmarkValueAsInteger = 0 ' return
    End If
    On Error GoTo 0
End Function

Private Sub UpdateBookmarkOfPrice(bookmarkName As String, value As Variant)
    On Error Resume Next ' Ignoruj bledy, jesli zakladka nie istnieje
    Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
    If Not bookmarkRange Is Nothing Then
        bookmarkRange.text = CStr(value)
        ' Dodaj zakladke na nowo, aby zachowac ja po zmianie tekstu
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Zakladka '" & bookmarkName & "' nie istnieje.", vbExclamation, "Error"
    End If
    On Error GoTo 0
End Sub

Private Sub Document_ContentControlOnExit(ByVal ContentControl As ContentControl, Cancel As Boolean)
    ' Sprawdz, czy zmieniony formant to "termin_od" lub "termin_do"
    Dim cc As ContentControl
    If ContentControl.Tag = "termin_od" Then
        ' Znajdz formant "termin_od_transport" i skopiuj datê
        For Each cc In ThisDocument.ContentControls
            If cc.Tag = "termin_od_transport" Then
                cc.Range.text = ContentControl.Range.text
                Exit For
            End If
        Next cc
    ElseIf ContentControl.Tag = "termin_do" Then
        ' Znajdz formant "termin_do_transport" i skopiuj datê
        'Dim cc As ContentControl
        For Each cc In ThisDocument.ContentControls
            If cc.Tag = "termin_do_transport" Then
                cc.Range.text = ContentControl.Range.text
                Exit For
            End If
        Next cc
    End If
End Sub

Private Sub AddPickupAndDropOffLocations()
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
        MsgBox "Nie znaleziono wyników dla zapytania.", vbExclamation, "B³¹d"
    End If
    
    ' Zamkniêcie po³¹czenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
End Sub

Private Sub InsertTextToContentControl(ByVal controlTitle As String, ByVal text As String)
    Dim cc As ContentControl
    
    ' ZnajdŸ formant zawartoœci i wstaw tekst
    For Each cc In ThisDocument.ContentControls
        If cc.Title = controlTitle Then
            cc.Range.text = text
            Exit For
        End If
    Next cc
End Sub
