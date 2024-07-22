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
    'Dim form As WyborWycieczkiZListyForm
    'Set form = New WyborWycieczkiZListyForm
    'form.Show ' Wyswietlenie UserForma
    'Unload form ' Zwolnienie zasobow po zakonczeniu
    WyborWycieczkiZListyForm.Show
End Sub

Sub Wprowadz_Ilosc_Osob()
    'Dim form As WprowadzLiczbeOsobForm
    'Set form = New WprowadzLiczbeOsobForm
    'form.Show ' Wyswietlenie UserForma
    'Unload form ' Zwolnienie zasobow po zakonczeniu
    
    WprowadzLiczbeOsobForm.Show
    UpdateTotalPrice
End Sub

Sub Przelicz_Kwoty()
    UpdateTotalAmount
End Sub

Sub Wprowadz_zamawiajacego()
    'Dim form As DodajZamawiajacegoForm
    'Set form = New DodajZamawiajacegoForm
    'form.Show ' Wyswietlenie UserForma
    'Unload form ' Zwolnienie zasobow po zakonczeniu
    
    DodajZamawiajacegoForm.Show
    
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

Sub Wstaw_ilosc_osob()
    ' Deklaracja zmiennej dla pola
    Dim fld As Field
    Dim rng As Range
    
    ' Pobranie bie¿¹cej pozycji kursora
    Set rng = Selection.Range
    
    ' Wstawienie pola DocProperty w bie¿¹cej pozycji kursora
    Set fld = ActiveDocument.Fields.Add(rng, wdFieldDocProperty, "ile_osob", False)
    
    ' Aktualizacja pola
    fld.Update
End Sub

Private Sub Document_ContentControlOnExit(ByVal ContentControl As ContentControl, Cancel As Boolean)
    ' Sprawdz, czy zmieniony formant to "termin_od" lub "termin_do"
    Dim cc As ContentControl
    If ContentControl.Tag = "termin_od" Then
        UpdateTransportDate ContentControl, "termin_od_transport"
    ElseIf ContentControl.Tag = "termin_do" Then
        UpdateTransportDate ContentControl, "termin_do_transport"
    ElseIf Right(ContentControl.Title, 17) = "_cena_jednostkowa" Then
        UpdateTotalPriceInline ContentControl
    ElseIf Right(ContentControl.Title, 6) = "_ilosc" Then
        UpdateTotalPriceInlineForQuantity ContentControl
    End If
End Sub


Private Sub Document_Open()
    ' Aktualizacja formantu datowego "data_dzis" na dzisiejsza date
    Call UpdateTodayDate
End Sub
