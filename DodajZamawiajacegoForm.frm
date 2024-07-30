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
Dim ignoreChangeEvent As Boolean ' Flaga do ignorowania zdarzen zmiany w ComboBoxie

Private Sub UserForm_Initialize()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    ' Inicjalnie nie ignorujemy zdarzen
    ignoreChangeEvent = False

    ' Inicjalizacja polaczenia z baza danych
    InitializeConnection
    
    ' Zapytanie SQL do pobrania danych
    Dim sqlQuery As String
    sqlQuery = "SELECT id, nazwa_instytucji FROM klienci;"
    
    ' Zaladuj wszystkie dane na poczatku
    LoadDataIntoComboBox ""
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Private Sub LoadDataIntoComboBox(searchText As String)
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    ' Blokowanie zdarzen zmian w ComboBoxie
    ignoreChangeEvent = True
    
    ' Zapytanie SQL do pobrania danych
    Dim sqlQuery As String
    sqlQuery = "SELECT id, nazwa_instytucji FROM klienci;"
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(sqlQuery)
    
    ' Przechowywanie zaznaczonego tekstu
    Dim selectedText As String
    selectedText = Trim(Me.cboClient.text)
    
    ' Czyszczenie ComboBoxa
    Me.cboClient.Clear
    
    ' Wczytanie wynikow do ComboBoxa
    Do While Not rs.EOF
        ' Filtruj wyniki na podstawie wyszukiwanego tekstu
        If InStr(1, rs.Fields("nazwa_instytucji").value, searchText, vbTextCompare) > 0 Then
            ' Dodaj zamawiajacego do ComboBoxa
            Me.cboClient.AddItem rs.Fields("nazwa_instytucji").value
        End If
        rs.MoveNext
    Loop
    
    ' Przywracanie zaznaczonego tekstu (jesli istnieje)
    If selectedText <> "" Then
        Dim i As Long
        For i = 0 To Me.cboClient.ListCount - 1
            If Me.cboClient.List(i) = selectedText Then
                Me.cboClient.ListIndex = i
                Exit For
            End If
        Next i
    End If
    
    ' Zamkniecie zestawu wynikowego
    rs.Close
    Set rs = Nothing
    
    ' Odblokowanie zdarzen zmian w ComboBoxie
    ignoreChangeEvent = False
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad podczas ladowania danych do ComboBoxa: " & Err.Description, vbCritical, "Error"
    If Not rs Is Nothing Then
        rs.Close
        Set rs = Nothing
    End If
    ignoreChangeEvent = False
End Sub

Private Sub cboClient_Change()
    ' Sprawdzenie czy zdarzenie ma byc ignorowane
    If ignoreChangeEvent Then Exit Sub
    
    ' Pobranie tekstu wprowadzonego przez uzytkownika
    Dim searchText As String
    searchText = Trim(Me.cboClient.text)
    
    ' Filtrowanie tekstu
    LoadDataIntoComboBox searchText
End Sub


Private Sub btnSelect_Click()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    Dim selectedItemIndex As Integer
    
    ' Pobierz wybrany indeks elementu z ComboBoxa
    selectedItemIndex = Me.cboClient.ListIndex
    
    ' Sprawdz, czy wybrano element z ComboBoxa
    If selectedItemIndex = -1 Then
        MsgBox "Wybierz wycieczke z listy.", vbExclamation, "Error"
        Exit Sub
    End If
    
    Dim selectedText As String
    selectedText = Me.cboClient.text
        
    ' Zapytanie SQL
    Dim sqlQuery As String
    sqlQuery = "SELECT id, nazwa_instytucji, ulica, numer, kod_pocztowy, " & _
             "miejscowosc FROM klienci WHERE nazwa_instytucji = '" & selectedText & "';"
           
    ' Zmienna dla zestawu wynikow
    Dim rs As Object
    Set rs = conn.Execute(sqlQuery)

    ' Dodanie obiektu klasy ClientClass
    If Not rs.EOF Then
        Set client = New ClientClass
        client.id = rs.Fields("id").value
        client.institutionName = rs.Fields("nazwa_instytucji").value
        client.street = rs.Fields("ulica").value
        client.number = rs.Fields("numer").value
        client.postalCode = rs.Fields("kod_pocztowy").value
        client.city = rs.Fields("miejscowosc").value
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
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub
