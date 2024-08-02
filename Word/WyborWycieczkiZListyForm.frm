VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} WyborWycieczkiZListyForm 
   Caption         =   "Wybór wycieczki z listy"
   ClientHeight    =   2010
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6210
   OleObjectBlob   =   "WyborWycieczkiZListyForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "WyborWycieczkiZListyForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim dictComboBoxItems As Scripting.Dictionary ' Globalny slownik

Private Sub UserForm_Initialize()
    Set dictComboBoxItems = New Scripting.Dictionary
    
    ' Inicjalizacja polaczenia z baza danych
    InitializeConnection
    
    ' Zapytanie SQL do pobrania danych
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki FROM wycieczki;"
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(strSQL)
    
    ' Wczytanie wynikow do ComboBoxa i slownika
    Dim itemIndex As Long
    itemIndex = 0
    Do While Not rs.EOF
        ' Dodaj wycieczke do ComboBoxa
        Me.WycieczkiComboBox.AddItem rs.Fields("nr").value & " - " & rs.Fields("nazwa_wycieczki").value
        
        ' Mapowanie indeksu na nr
        dictComboBoxItems.Add itemIndex, CStr(rs.Fields("nr").value)
        itemIndex = itemIndex + 1
        
        rs.MoveNext
    Loop
    
    ' Zamkniecie zestawu wynikowego i polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
End Sub

Private Sub WybierzCommandButton_Click()
    Dim selectedItemIndex As Long
    Dim selectedNr As String
    
    ' Pobierz wybrany indeks elementu z ComboBoxa
    selectedItemIndex = Me.WycieczkiComboBox.ListIndex
    
    ' Sprawdz, czy wybrano element z ComboBoxa
    If selectedItemIndex = -1 Then
        MsgBox "Wybierz wycieczke z listy.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Pobierz wybrany nr ze slownika
    If dictComboBoxItems.Exists(selectedItemIndex) Then
        selectedNr = dictComboBoxItems(selectedItemIndex)
    Else
        MsgBox "Nie znaleziono wybranego elementu w slowniku.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Inicjalizacja polaczenia z baza danych
    InitializeConnection
    
    ' Zapytanie SQL
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki, cena_bazowa, bilety_wstepu FROM wycieczki WHERE nr = '" & selectedNr & "';"

    ' Zmienna dla zestawu wynikow
    Dim rs As Object
    Set rs = conn.Execute(strSQL)

    ' Dodanie obiektu klasy Wycieczka
    If Not rs.EOF Then
        Set wycieczka = New KlasaWycieczka
        wycieczka.nr = rs.Fields("nr").value
        wycieczka.nazwa_wycieczki = rs.Fields("nazwa_wycieczki").value
        wycieczka.cena_bazowa = rs.Fields("cena_bazowa").value
        wycieczka.bilety_wstepu = rs.Fields("bilety_wstepu").value
    Else
        MsgBox "Nie znaleziono wycieczki o podanym nr.", vbExclamation, "Error"
        rs.Close
        conn.Close
        Exit Sub
    End If
        
    ' Zamkniecie zestawu wynikowego i polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
    ' Wstaw dane do zakladek w dokumencie Word
    ' Wstaw dane do formantow w dokumencie Word
    InsertTextToContentControl "nr_wycieczki", wycieczka.nr
    InsertTextToContentControl "nazwa_wycieczki", wycieczka.nazwa_wycieczki
    
    'AddDataToBookmark "nr", wycieczka.nr
    'AddDataToBookmark "nazwa_wycieczki", wycieczka.nazwa_wycieczki
    'AddDataToBookmark "cena_bazowa", wycieczka.cena_bazowa
    'AddDataToBookmark "bilety_wstepu", wycieczka.bilety_wstepu
        
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me
End Sub

'Private Sub AddDataToBookmark(bookmarkName As String, bookmarkValue As Variant)
   ' Set doc = ThisDocument ' Ustaw biezacy dokument Word
    
    ' Sprawdz czy istnieje zakladka o podanej nazwie
'    If doc.Bookmarks.Exists(bookmarkName) Then
    
        ' Uzyskaj zakres zak³adki
 '       Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
        
        ' Wstaw podana wartosc do zakladki
  '      If VarType(bookmarkValue) = vbInteger Then
   '         bookmarkRange.text = CStr(bookmarkValue) ' Konwertuj Integer na String
    '    Else
     '       bookmarkRange.text = bookmarkValue
        'End If
        
        ' Dodaj zakladke na nowo
        'doc.Bookmarks.Add bookmarkName, bookmarkRange
    'Else
     '   MsgBox "Nie mozna znalezc zakladki """ & bookmarkName & """ w dokumencie.", vbExclamation, "Error"
    'End If
'End Sub
