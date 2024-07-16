VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "AccessDatabaseConfig"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
' Deklaracja klasy AccessDatabaseConfig
Private m_Path As String

' W³aœciwosæ Path
Public Property Get Path() As String
    Path = m_Path
End Property

Public Property Let Path(ByVal value As String)
    m_Path = value
End Property

' W³aœciwoœæ ConnectionString
Public Property Get ConnectionString() As String
    ConnectionString = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" & m_Path & ";"
End Property


