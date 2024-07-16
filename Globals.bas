Attribute VB_Name = "Globals"
Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne po³¹czenie z baz¹ danych
Public config As AccessDatabaseConfig ' Globalna konfiguracja bazy danych Access
Public Const DATABASE_PATH As String = "E:\Dokumenty z C\US\BP Wesley\umowy-bp.accdb"
Public wycieczka As KlasaWycieczka ' Globalny obiekt konkretnej wycieczki
Public bookmarkRange As Range ' Globalna zmienna do lokalizacji zak³adek

Public Sub InitializeConnection()
    ' Inicjalizacja po³¹czenia z baz¹ danych
    Set config = New AccessDatabaseConfig
    config.Path = DATABASE_PATH
    
    ' Uzyskaj ci¹g po³¹czenia do bazy danych Access
    Dim strConnection As String
    strConnection = config.ConnectionString
    
    ' Tworzenie nowego po³¹czenia
    Set conn = New ADODB.Connection
    conn.Open strConnection
End Sub


