VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ThisDocument"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Sub WybierzWycieczke()
    Dim form As WyborWycieczkiZListyForm
    Set form = New WyborWycieczkiZListyForm
    
    ' Wyœwietlenie UserFormy (modalne)
    form.Show ' U¿yj form.ShowModal, jeœli chcesz, aby UserForma by³a modalna
    
    ' Zwolnienie zasobów po zakoñczeniu
    Unload form
    
End Sub

Sub WprowadzIloscOsob()
    Dim form As WprowadzLiczbeOsobForm
    Set form = New WprowadzLiczbeOsobForm
    
    form.Show
    
    Unload form
End Sub

Sub WyliczKwoty()
    Set doc = ThisDocument
    
    ' Pobierz iloœæ osób z zak³adki
    Dim numberOfPeople As Long
    numberOfPeople = GetBookmarkValueAsInteger("ile_osob")
    
    ' Pobierz wartoœci z zak³adek i przelicz
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
    
    ' Zaktualizuj zak³adki z przeliczonymi wartoœciami
    UpdateBookmarkOfPrice "cena_bazowa_suma", basePriceTotal
    UpdateBookmarkOfPrice "bilety_wstepu_suma", entryTicketsTotal
    UpdateBookmarkOfPrice "razem_kwota_brutto", totalAmount
End Sub

Private Function GetBookmarkValueAsInteger(bookmarkName As String) As Long
    On Error Resume Next ' Ignoruj b³êdy, jeœli zak³adka nie istnieje
    
    'Dim bookmarkRange
    Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
    If Not bookmarkRange Is Nothing Then
        GetBookmarkValueAsInteger = CLng(bookmarkRange.Text) ' return
    Else
        MsgBox "Zak³adka '" & bookmarkName & "' nie istnieje.", vbExclamation, "B³¹d"
        GetBookmarkValueAsInteger = 0 ' return
    End If
    On Error GoTo 0
End Function

Private Sub UpdateBookmarkOfPrice(bookmarkName As String, value As Variant)
    On Error Resume Next ' Ignoruj b³êdy, jeœli zak³adka nie istnieje
    'Dim bookmarkRange As Range
    Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
    
    If Not bookmarkRange Is Nothing Then
        bookmarkRange.Text = CStr(value)
        ' Dodaj zak³adkê na nowo, aby zachowaæ j¹ po zmianie tekstu
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Zak³adka '" & bookmarkName & "' nie istnieje.", vbExclamation, "B³¹d"
    End If
    On Error GoTo 0
End Sub

