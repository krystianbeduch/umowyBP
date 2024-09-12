VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} WybierzKlientaForm 
   Caption         =   "Dodaj zamawiajacego"
   ClientHeight    =   2010
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6210
   OleObjectBlob   =   "WybierzKlientaForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "WybierzKlientaForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Dim ignoreChangeEvent As Boolean ' Flaga do ignorowania zdarzen zmiany w ComboBoxie

Private Sub UserForm_Initialize()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    ' Flaga prawidlowo zatwierdzonych danych
    Wprowadz_klienta_IsDataEntered = False
    
    ' Inicjalnie nie ignorujemy zdarzen
    ignoreChangeEvent = False

    ' Inicjalizacja polaczenia z baza danych PostgreSQL
    If Not InitializeConnection_PostgreSQL() Then
        MsgBox "Nie udalo sie nawiazac polaczenia z baza danych PostgreSQL.", vbCritical, "Error"
        Exit Sub
    End If
    
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
    
    Dim sqlProvider As New SQLQueryProvider
    
    ' Wykonanie zapytania SQL
    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(sqlProvider.SelectClient_NumberNameAlias)
    
    ' Przechowywanie zaznaczonego tekstu
    Dim selectedText As String
    selectedText = Trim(Me.cboClient.text)
    
    ' Czyszczenie ComboBoxa
    Me.cboClient.Clear
    
    ' Wczytanie wynikow do ComboBoxa
    Do While Not rs.EOF
        ' Filtruj wyniki na podstawie wyszukiwanego tekstu
        If InStr(1, rs.Fields("name").value, searchText, vbTextCompare) > 0 Or _
           InStr(1, rs.Fields("alias").value, searchText, vbTextCompare) > 0 Then
            ' Dodaj zamawiajacego do ComboBoxa
            Me.cboClient.AddItem rs.Fields("name").value
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
    Dim sqlProvider As New SQLQueryProvider
           
    ' Zmienna dla zestawu wynikow
    Dim rs As Object
    Set rs = conn.Execute(sqlProvider.SelectClient(selectedText))

    ' Dodanie obiektu klasy ClientClass
    If Not rs.EOF Then
        Set client = New ClientClass
        client.clientNumber = rs.Fields("client_number").value
        client.name = rs.Fields("name").value
        client.alias = rs.Fields("alias").value
        client.street = rs.Fields("street").value
        client.number = rs.Fields("number").value
        client.postCode = rs.Fields("post_code").value
        client.city = rs.Fields("city").value
    Else
        MsgBox "Nie znaleziono klienta o podanym numerze.", vbExclamation, "Error"
        rs.Close
        conn.Close
        Exit Sub
    End If
        
    ' Zamkniecie zestawu wynikowego i polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
    ' Ustaw flage prawidlowo wprowadzonych danych
    Wprowadz_klienta_IsDataEntered = True
    
    ' Zamknij UserForma
    Unload Me
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub
