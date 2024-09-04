VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "SQLQueryProvider"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Public Property Get SelectId_NazwaInstytucji() As String
    SelectId_NazwaInstytucji = "SELECT id, nazwa_instytucji " & _
                               "FROM ""bpWesley"".klienci;"
End Property

Public Property Get SelectClient(selectedText As String) As String
    SelectClient = "SELECT " & _
                        "id, " & _
                        "nazwa_instytucji, " & _
                        "ulica, " & _
                        "numer, " & _
                        "kod_pocztowy, " & _
                        "miejscowosc " & _
                   "FROM ""bpWesley"".klienci " & _
                   "WHERE nazwa_instytucji = '" & selectedText & "';"
End Property

Public Property Get SelectClientWithPickupLocation(clientId As Integer) As String
    SelectClientWithPickupLocation = "SELECT " & _
                                        "miejsce_odbioru, " & _
                                        "miejscowosc, " & _
                                        "ulica, " & _
                                        "numer, " & _
                                        "miejscowosc_miejsca_odbioru, " & _
                                        "ulica_miejsca_odbioru, " & _
                                        "numer_miejsca_odbioru, " & _
                                        "kod_pocztowy_miejsca_odbioru " & _
                                     "FROM ""bpWesley"".klienci " & _
                                     "WHERE id = " & clientId & ";"
End Property
