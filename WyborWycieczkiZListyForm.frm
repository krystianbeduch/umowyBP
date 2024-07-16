VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} WyborWycieczkiZListyForm 
   Caption         =   "Wyb�r wycieczki z listy"
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
Dim dictComboBoxItems As Scripting.Dictionary ' Globalny s�ownik

Private Sub UserForm_Initialize()
    Set dictComboBoxItems = New Scripting.Dictionary
    
    ' Inicjalizacja po��czenia z baz� danych
    InitializeConnection
    
    ' Zapytanie SQL do pobrania danych
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki FROM wycieczki;"
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynik�w
    Set rs = conn.Execute(strSQL)
    
    ' Wczytanie wynik�w do ComboBoxa i s�ownika
    Dim itemIndex As Long
    itemIndex = 0
    Do While Not rs.EOF
        ' Dodaj wycieczk� do ComboBoxa
        Me.WycieczkiComboBox.AddItem rs.Fields("nr").value & " - " & rs.Fields("nazwa_wycieczki").value
        
        ' Mapowanie indeksu na nr
        dictComboBoxItems.Add itemIndex, CStr(rs.Fields("nr").value)
        itemIndex = itemIndex + 1
        
        rs.MoveNext
    Loop
    
    ' Zamkni�cie zestawu wynikowego i po��czenia
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
    
    ' Sprawd�, czy wybrano element z ComboBoxa
    If selectedItemIndex = -1 Then
        MsgBox "Prosz� wybra� wycieczk� z listy.", vbExclamation, "B��d"
        Exit Sub
    End If
    
    ' Pobierz wybrany nr ze s�ownika
    If dictComboBoxItems.Exists(selectedItemIndex) Then
        selectedNr = dictComboBoxItems(selectedItemIndex)
    Else
        MsgBox "B��d: nie znaleziono wybranego elementu w s�owniku.", vbExclamation, "B��d"
        Exit Sub
    End If
    
    ' Inicjalizacja po��czenia z baz� danych
    InitializeConnection
    
    ' Zapytanie SQL
    Dim strSQL As String
    strSQL = "SELECT nr, nazwa_wycieczki, cena_bazowa, bilety_wstepu FROM wycieczki WHERE nr = '" & selectedNr & "';"

    ' Zmienna dla zestawu wynik�w
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
        MsgBox "Nie znaleziono wycieczki o podanym nr.", vbExclamation, "B��d"
        rs.Close
        conn.Close
        Exit Sub
    End If
        
    ' Zamkni�cie zestawu wynikowego i po��czenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
    ' Wstaw dane do zak�adek w dokumencie Word
    AddDataToBookmark "nr", wycieczka.nr
    AddDataToBookmark "nazwa_wycieczki", wycieczka.nazwa_wycieczki
    AddDataToBookmark "cena_bazowa", wycieczka.cena_bazowa
    AddDataToBookmark "bilety_wstepu", wycieczka.bilety_wstepu
    
    ' Wstaw date do zak�adki
    AddDate "data_dzis"
    
    ' Zamknij UserForma po wstawieniu numeru
    Unload Me
End Sub

Private Sub AddDataToBookmark(bookmarkName As String, bookmarkValue As Variant)
    Set doc = ThisDocument ' Ustaw bie��cy dokument Word
    
    ' Sprawd� czy istnieje zak�adka o podanej nazwie
    If doc.Bookmarks.Exists(bookmarkName) Then
    
        ' Uzyskaj zakres zak�adki
        'Dim bookmarkRange As Range
        Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
        
        ' Wstaw podana wartosc do zak�adki
        If VarType(bookmarkValue) = vbInteger Then
            bookmarkRange.Text = CStr(bookmarkValue) ' Konwertuj Integer na String
        Else
            bookmarkRange.Text = bookmarkValue
        End If
        
        ' Dodaj zak�adk� na nowo
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Nie mo�na znale�� zak�adki """ & bookmarkName & """ w dokumencie.", vbExclamation, "B��d"
    End If
End Sub

Private Sub AddDate(bookmarkName As String)
    Set doc = ThisDocument
    
    ' Sprawd� czy istnieje zak�adka o podanej nazwie
    If doc.Bookmarks.Exists(bookmarkName) Then
    
        ' Uzyskaj zakres zak�adki
        'Dim bookmarkRange As Range
        Set bookmarkRange = doc.Bookmarks(bookmarkName).Range
        
        bookmarkRange.Text = Format(Date, "dd.mm.yyyy")
                
        ' Dodaj zak�adk� na nowo
        doc.Bookmarks.Add bookmarkName, bookmarkRange
    Else
        MsgBox "Nie mo�na znale�� zak�adki """ & bookmarkName & """ w dokumencie.", vbExclamation, "B��d"
    End If
End Sub
