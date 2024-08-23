Attribute VB_Name = "Globals"
Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne polaczenie z baza danych
Public config As AccessDatabaseConfig ' Globalna konfiguracja bazy danych Access
Public Const DATABASE_PATH As String = "E:\Dokumenty z C\US\BP Wesley\umowy-bp.accdb" ' Sciezka do pliku bazy danych
Public Const DOC_PROP_NUMBER_OF_PEOPLE As String = "ile_osob"
Public wycieczka As KlasaWycieczka ' Globalny obiekt konkretnej wycieczki
Public client As ClientClass ' Globalny obiekt klienta

Public Wprowadz_zamawiajacego_IsDataEntered As Boolean
Public typeOfTour As Boolean ' Typ wycieczki: true - szkolna, false - dorosli (domyslnie False)
Public locationOfTour As Boolean ' Lokalizacja wycieczki: true - Polska, false - Europa
Public isMultiDay As Boolean ' true - wycieczka wielodniowa, false - wycieczka 1-dniowa

Public Sub InitializeConnection()
    ' Inicjalizacja polaczenia z baza danych
    Set config = New AccessDatabaseConfig
    config.Path = DATABASE_PATH
    
    ' Uzyskaj ciag polaczenia do bazy danych Access
    Dim strConnection As String
    strConnection = config.ConnectionString
    
    ' Tworzenie nowego polaczenia
    Set conn = New ADODB.Connection
    conn.Open strConnection
End Sub

Public Function Ceiling(ByVal value As Currency) As Currency
    ' Funkcja zakraglacaja do gory
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

