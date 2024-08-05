Attribute VB_Name = "Globals"
Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne polaczenie z baza danych
Public config As AccessDatabaseConfig ' Globalna konfiguracja bazy danych Access
Public Const DATABASE_PATH As String = "E:\Dokumenty z C\US\BP Wesley\umowy-bp.accdb" ' Sciezka do pliku bazy danych
Public wycieczka As KlasaWycieczka ' Globalny obiekt konkretnej wycieczki
Public client As ClientClass ' Globalny obiekt klienta
Public Wprowadz_zamawiajacego_IsDataEntered As Boolean

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

