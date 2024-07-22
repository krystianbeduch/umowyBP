VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} DodajZamawiajacegoForm 
   Caption         =   "Dodaj zamawiajacego"
   ClientHeight    =   2010
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6210
   OleObjectBlob   =   "DodajZamawiajacegoForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "DodajZamawiajacegoForm"
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
    strSQL = "SELECT id, imie, nazwisko, nazwa_instytucji FROM klienci;"
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(strSQL)
    
    ' Wczytanie wynikow do ComboBoxa i slownika
    Dim itemIndex As Long
    itemIndex = 0
    Do While Not rs.EOF
        ' Dodaj zamawiajacych do ComboBoxa
        Me.ZamawiajacyComboBox.AddItem rs.Fields("nazwa_instytucji").value & " - " & _
                                        rs.Fields("imie").value & " " & rs.Fields("nazwisko").value
        
        ' Mapowanie indeksu na nr
        dictComboBoxItems.Add itemIndex, rs.Fields("id").value
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
    selectedItemIndex = Me.ZamawiajacyComboBox.ListIndex
    
    ' Sprawdz, czy wybrano element z ComboBoxa
    If selectedItemIndex = -1 Then
        MsgBox "Wybierz wycieczke z listy.", vbExclamation, "Error"
        Exit Sub
    End If
    
    ' Pobierz wybrany nr ze s³ownika
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
    strSQL = "SELECT id, imie, nazwisko, nazwa_instytucji, ulica, " & _
             "numer, kod_pocztowy, miejscowosc FROM klienci WHERE id = " & selectedNr & ";"
           
    ' Zmienna dla zestawu wynikow
    Dim rs As Object
    Set rs = conn.Execute(strSQL)

    ' Dodanie obiektu klasy Wycieczka
    If Not rs.EOF Then
        Set zamawiajacy = New KlasaZamawiajacy
        zamawiajacy.id = rs.Fields("id").value
        zamawiajacy.imie = rs.Fields("imie").value
        zamawiajacy.nazwisko = rs.Fields("nazwisko").value
        zamawiajacy.nazwa_instytucji = rs.Fields("nazwa_instytucji").value
        zamawiajacy.ulica = rs.Fields("ulica").value
        zamawiajacy.numer = rs.Fields("numer").value
        zamawiajacy.kod_pocztowy = rs.Fields("kod_pocztowy").value
        zamawiajacy.miejscowosc = rs.Fields("miejscowosc").value
    Else
        MsgBox "Nie znaleziono zamawiajacego o podanym id.", vbExclamation, "Error"
        rs.Close
        conn.Close
        Exit Sub
    End If
        
    ' Zamkniecie zestawu wynikowego i polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
    ' Zamknij UserForma
    Unload Me
End Sub
