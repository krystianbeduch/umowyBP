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
    SelectId_NazwaInstytucji = "SELECT client_number, name " & _
                               "FROM clients;"
End Property

Public Property Get SelectClient(selectedText As String) As String
    SelectClient = "SELECT " & _
                        "client_number, " & _
                        "name, " & _
                        "street, " & _
                        "number, " & _
                        "post_code, " & _
                        "city " & _
                   "FROM clients " & _
                   "WHERE name = '" & selectedText & "';"
End Property

Public Property Get SelectClientWithPickupLocation(clientNumber As Integer) As String
    SelectClientWithPickupLocation = "SELECT " & _
                                        "pickup_location, " & _
                                        "city, " & _
                                        "street, " & _
                                        "number, " & _
                                        "pickup_city, " & _
                                        "pickup_street, " & _
                                        "pickup_number, " & _
                                        "pickup_post_code " & _
                                     "FROM clients " & _
                                     "WHERE client_number = " & clientNumber & ";"
End Property
