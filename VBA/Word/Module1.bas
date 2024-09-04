Attribute VB_Name = "Module1"
Sub AddPickupAndDropOffLocations()
    ' Inicjalizacja polaczenia z baza danych
    If Not InitializeConnection_PostgreSQL() Then
        MsgBox "Nie udalo sie nawiazac polaczenia z baza danych PostgreSQL.", vbCritical, "Error"
        Exit Sub
    End If
    
    ' Zapytanie SQL do pobrania danych
    Dim sqlProvider As New SQLQueryProvider

    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(sqlProvider.SelectClientWithPickupLocation(client.id))
    
    ' Sprawdz wynik i utworz tekst do wstawienia do formantow
    If Not rs.EOF Then
        If rs.Fields("miejsce_odbioru").value = "*Adres*" Then
            ' Jesli "miejsce_odbioru" jest zgodne ze wzrrcem "*Adres" - jest takie samo jak adres
            With client.pickupLocation
                .pickupLocation = ""
                .city = client.city
                .street = client.street
                .number = client.number
                .postalCode = client.postalCode
            End With
        Else
            ' Miejsce odbioru jest ustalone w innym miejscu
            With client.pickupLocation
                .pickupLocation = IIf(IsNull(rs.Fields("miejsce_odbioru").value), "", rs.Fields("miejsce_odbioru").value)
                .city = IIf(IsNull(rs.Fields("miejscowosc_miejsca_odbioru").value), "", rs.Fields("miejscowosc_miejsca_odbioru").value)
                .street = IIf(IsNull(rs.Fields("ulica_miejsca_odbioru").value), "", rs.Fields("ulica_miejsca_odbioru").value)
                .number = IIf(IsNull(rs.Fields("numer_miejsca_odbioru").value), "", rs.Fields("numer_miejsca_odbioru").value)
                .postalCode = IIf(IsNull(rs.Fields("kod_pocztowy_miejsca_odbioru").value), "", rs.Fields("kod_pocztowy_miejsca_odbioru").value)
            End With
        End If
    Else
        MsgBox "Nie znaleziono wynikow dla zapytania.", vbExclamation, "Error"
    End If
    
    ' Zamkniecie polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
End Sub


