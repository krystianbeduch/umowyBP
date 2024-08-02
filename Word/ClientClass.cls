VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "ClientClass"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy ClientClass
Private m_id As Integer
Private m_institutionName As String
Private m_street As String
Private m_number As String
Private m_postalCode As String
Private m_city As String
Private m_pickupLocation As PickupLocationClass

' Wlasciwosc id
Public Property Get id() As String
    id = m_id
End Property
Public Property Let id(value As String)
    m_id = value
End Property

' Wlasciwosc institutionName
Public Property Get institutionName() As String
    institutionName = m_institutionName
End Property
Public Property Let institutionName(value As String)
    m_institutionName = value
End Property

' Wlasciwosc street
Public Property Get street() As String
    street = m_street
End Property
Public Property Let street(value As String)
    m_street = value
End Property

' Wlasciwosc number
Public Property Get number() As String
    number = m_number
End Property
Public Property Let number(value As String)
    m_number = value
End Property

' Wlasciwosc postalCode
Public Property Get postalCode() As String
    postalCode = m_postalCode
End Property
Public Property Let postalCode(value As String)
    m_postalCode = value
End Property

' Wlasciwosc city
Public Property Get city() As String
    city = m_city
End Property
Public Property Let city(value As String)
    m_city = value
End Property

' W³aœciwoœæ pickupLocation
Public Property Get pickupLocation() As PickupLocationClass
    Set pickupLocation = m_pickupLocation
End Property
Public Property Set pickupLocation(value As PickupLocationClass)
    Set m_pickupLocation = value
End Property

' Inicjalizacja obiektu pickupLocation w konstruktorze
Private Sub Class_Initialize()
    Set m_pickupLocation = New PickupLocationClass
End Sub

