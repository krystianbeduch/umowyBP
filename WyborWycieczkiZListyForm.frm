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
Dim dictComboBoxItems As Scripting.Dictionary ' Globalny s³ownik

Private Sub UserForm_Initialize()
    Set dictComboBoxItems = New Scripting.Dictionary
    
    ' Inicjalizacja po³¹czenia z baz¹ danych
    InitializeConnection
    
    ' Zapytanie SQL do pobrania danych
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki FROM wycieczki;"
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wyników
    Set rs = conn.Execute(strSQL)
    
    ' Wczytanie wyników do ComboBoxa i s³ownika
    Dim itemIndex As Long
    itemIndex = 0
    Do While Not rs.EOF
        ' Dodaj wycieczkê do ComboBoxa
        Me.WycieczkiComboBox.AddItem rs.Fields("nr").value & " - " & rs.Fields("nazwa_wycieczki").value
        
        ' Mapowanie indeksu na nr
        dictComboBoxItems.Add itemIndex, CStr(rs.Fields("nr").value)
        itemIndex = itemIndex + 1
        
        rs.MoveNext
    Loop
    
    ' Zamkniêcie zestawu wynikowego i po³¹czenia
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
    
    ' SprawdŸ, czy wybrano element z ComboBoxa
    If selectedItemIndex = -1 Then
        MsgBox "Proszê wybraæ wycieczkê z listy.", vbExclamation, "B³¹d"
        Exit Sub
    End If
    
    ' Pobierz wybrany nr ze s³ownika
    If dictComboBoxItems.Exists(selectedItemIndex) Then
        selectedNr = dictComboBoxItems(selectedItemIndex)
    Else
        MsgBox "B³¹d: nie znaleziono wybranego elementu w s³owniku.", vbExclamation, "B³¹d"
        Exit Sub
    End If
    
    ' Inicjalizacja po³¹czenia z baz¹ danych
    InitializeConnection
    
    ' Zapytanie SQL
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki, cena_bazowa, bilety_wstepu FROM wycieczki WHERE nr = '" & selectedNr & "';"

    ' Zmienna dla zestawu wyników
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
        MsgBox "Nie znaleziono wycieczki o podanym nr.", vbExclamation, "B³¹d"
        rs.Close
        conn.Close
        Exit Sub
    End If
        
    ' Zamkniêcie zestawu wynikowego i po³¹czenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
    ' Wstaw dane do zak³adek w dokumencie Word
    AddDataToBookmark "nr", wycieczka.nr
    AddDataToBookmark "nazwa_wycieczki", wycieczka.nazwa_wycieczki
    AddDataToBookmark "cena_bazowa", wycieczka.cena_bazowa
    AddDataToBookmark "bilety_wstepu", wycieczka.bilety_wstepu
    
    ' Wstaw date do zak³adki
    AddDate "data_dzis"
    
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me
End Sub

Private Sub AddDataToBookmark(bookmarkName As String, bookmarkValue As Variant)
    Set doc = ThisDocument ' Ustaw bie¿¹cy dokument Word
    
    ' SprawdŸ czy istnieje zak³adka o podanej nazwie
    If doc.Bookmarks.Exists(bookmarkName) Then
    
        ' Uzyskaj zakres zak³adki
        'Dim bookmarkRange As Range
        Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
        
        ' Wstaw podana wartosc do zak³adki
        If VarType(bookmarkValue) = vbInteger Then
            bookmarkRange.Text = CStr(bookmarkValue) ' Konwertuj Integer na String
        Else
            bookmarkRange.Text = bookmarkValue
        End If
        
        ' Dodaj zak³adkê na nowo
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Nie mo¿na znaleŸæ zak³adki """ & bookmarkName & """ w dokumencie.", vbExclamation, "B³¹d"
    End If
End Sub

Private Sub AddDate(bookmarkName As String)
    Set doc = ThisDocument
    
    ' SprawdŸ czy istnieje zak³adka o podanej nazwie
    If doc.Bookmarks.Exists(bookmarkName) Then
    
        ' Uzyskaj zakres zak³adki
        'Dim bookmarkRange As Range
        Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
        
        bookmarkRange.Text = Format(Date, "dd.mm.yyyy")
                
        ' Dodaj zak³adkê na nowo
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Nie mo¿na znaleŸæ zak³adki """ & bookmarkName & """ w dokumencie.", vbExclamation, "B³¹d"
    End If
End Sub
