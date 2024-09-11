Attribute VB_Name = "Globals"
Option Explicit

Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne polaczenie z baza danych
Public Const DOC_PROP_NUMBER_OF_PEOPLE As String = "ile_osob"
Public client As ClientClass ' Globalny obiekt klienta

Public Wprowadz_klienta_IsDataEntered As Boolean
Public typeOfTour As Boolean ' Typ wycieczki: true - szkolna, false - dorosli (domyslnie False)
Public locationOfTour As Boolean ' Lokalizacja wycieczki: true - Polska, false - Europa
Public isMultiDay As Boolean ' true - wycieczka wielodniowa, false - wycieczka 1-dniowa

Public Sub InitializeConnection_Access()
    ' Inicjalizacja polaczenia z baza danych
    Dim config As New AccessDatabaseConfig
    config.Path = DATABASE_PATH ' sciezka do pliku .accdb
    
    ' Uzyskaj ciag polaczenia do bazy danych Access
    Dim strConnection As String
    strConnection = config.ConnectionString
    
    ' Tworzenie nowego polaczenia
    Set conn = New ADODB.Connection
    conn.Open strConnection
End Sub

Public Function InitializeConnection_PostgreSQL() As Boolean
    ' Inicjalizacja polaczenia z baza danych PostgreSQL
    Dim config As New PostgreSQLDatabaseConfig
    config.Server = "localhost"
    config.Port = 5432
    config.Database = "postgres"
    config.User = "postgres"
    config.Password = "root"
    
    Dim strConnection As String
    strConnection = config.ConnectionString

    ' Otwarcie polaczenia
    On Error GoTo ErrorHandler
    Set conn = CreateObject("ADODB.Connection")
    conn.Open strConnection
    
  ' MsgBox "Polaczenie z baza danych PostgreSQL zostalo nawiazane"
    InitializeConnection_PostgreSQL = True
    Exit Function
    
ErrorHandler:
    MsgBox "Blad bazy: " & Err.Description, vbCritical, "Error"
    If conn.State = adStateOpen Then conn.Close
    InitializeConnection_PostgreSQL = False
End Function

Public Function Ceiling(ByVal value As Currency) As Currency
    ' Funkcja zaokraglajaca do gory
    Ceiling = Int(value)
    If value > Int(value) Then
        Ceiling = Ceiling + 1
    End If
End Function

Public Function GetDocProperty(propName As String) As Variant
    On Error Resume Next
    GetDocProperty = ActiveDocument.CustomDocumentProperties(propName).value
    On Error GoTo 0
End Function

Public Sub InsertTextToContentControl(controlName As String, text As String)
    On Error GoTo ErrorHandler
    Dim cc As ContentControl
    Dim controlFound As Boolean
    
    ' Znajdz formant zawartosci i wstaw tekst
    For Each cc In ActiveDocument.ContentControls
        If cc.Title = controlName Then
            cc.Range.text = text
            controlFound = True
            Exit Sub
        End If
    Next cc
    
    If Not controlFound Then
        MsgBox "Nie znaleziono formantu: " & controlName, vbExclamation, "Error"
    End If
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
    Exit Sub
End Sub

