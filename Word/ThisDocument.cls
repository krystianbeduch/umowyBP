VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ThisDocument"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True

'Sub Wybierz_Wycieczke()
    'WyborWycieczkiZListyForm.Show
'End Sub

Sub Wprowadz_Ilosc_Osob()
    WprowadzLiczbeOsobForm.Show
End Sub

Sub Przelicz_Kwoty()
    UpdateTotalAmount
End Sub

Sub Wprowadz_zamawiajacego()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    DodajZamawiajacegoForm.Show
    
    ' Sprawdz czy dane zostaly prawidlowo wprowadzone (czy zostala wybrana opcja)
    If Not DodajZamawiajacegoForm.IsDataEntered Then
        Exit Sub
    End If
    
    ' Wstaw dane do formantu "client"
    Dim resultText As String
    resultText = client.institutionName & ", " & client.street & " " & client.number & _
             ", " & client.postalCode & " " & client.city
    Call InsertTextToContentControl("zamawiajacy", resultText)
    
    ' Wstaw dane do formantu "miejsce_odjazdu" i "miejsce_powrotu"
    AddPickupAndDropOffLocations
    resultText = client.pickupLocation.postalCode & " " & client.pickupLocation.city & ", " & _
                 client.pickupLocation.pickupLocation & " " & client.pickupLocation.street & " " & _
                 client.pickupLocation.number
    Call InsertTextToContentControl("miejsce_odjazdu", resultText)
    Call InsertTextToContentControl("miejsce_powrotu", resultText)
    Exit Sub
    
ErrorHandler:
    MsgBox "Nie wybrano prawidlowo zamawiajacego: " & Err.Description, vbExclamation, "Error"
End Sub

Sub Wstaw_ilosc_osob()
    ' Deklaracja zmiennej dla pola
    Dim fld As Field
    Dim rng As Range
    
    ' Pobranie biezacej pozycji kursora
    Set rng = Selection.Range
    
    ' Wstawienie pola DocProperty w biezacej pozycji kursora
    Set fld = ActiveDocument.Fields.Add(rng, wdFieldDocProperty, "ile_osob", False)
    
    ' Aktualizacja pola
    fld.Update
End Sub

Sub Dodaj_usluge()
    DodajUslugeForm.Show
End Sub

Sub Dodaj_dodatkowo_platne()
    DodajDodatkowoPlatneForm.Show
End Sub

Private Sub Document_ContentControlOnExit(ByVal ContentControl As ContentControl, Cancel As Boolean)
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
    Call InitializePath
End Sub


