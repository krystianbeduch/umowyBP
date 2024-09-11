VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ThisDocument"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Option Explicit

Sub Ustaw_liczbe_osob()
    UstawLiczbeOsobForm.Show
End Sub

Sub Przelicz_Kwoty()
    UpdateTotalAmount
End Sub

Sub Wprowadz_klienta()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    WybierzKlientaForm.Show
    
    ' Sprawdz czy dane zostaly prawidlowo wprowadzone (czy zostala wybrana opcja)
    If Not Wprowadz_klienta_IsDataEntered Then
        Exit Sub
    End If
    
    ' Wstaw dane do formantu "klient"
    Dim resultText As String
    resultText = client.name & ", " & client.street & " " & client.number & _
             ", " & client.postCode & " " & client.city
    Call InsertTextToContentControl("klient", resultText)
    
    ' Wstaw dane do formantu "miejsce_odjazdu" i "miejsce_powrotu"
    AddPickupAndDropOffLocations
    resultText = client.PickupLocation.postCode & " " & client.PickupLocation.city & ", " & _
                 client.PickupLocation.PickupLocation & " " & client.PickupLocation.street & " " & _
                 client.PickupLocation.number
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

Sub Okresl_Rodzaj_Wycieczki()
    OkreslRodzajWycieczkiForm.Show
End Sub

Private Sub Document_ContentControlOnExit(ByVal ContentControl As ContentControl, Cancel As Boolean)
    If ContentControl.Title = "termin_wyjazdu" Then
        UpdateTransportDate ContentControl, "termin_od_transport"
        
    ElseIf ContentControl.Title = "termin_przyjazdu" Then
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


