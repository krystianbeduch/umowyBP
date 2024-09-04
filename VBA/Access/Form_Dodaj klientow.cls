VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Form_Dodaj klientow"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Compare Database
Option Explicit

Dim isSaveEnabled As Boolean ' Flaga kontroluj¹ca zapis

Private Sub btnAddClient_Click()
    'Sprawdzenie, czy wymagane pola s¹ wype³nione
    If IsNull(Me.txt_nazwa_instytucji) Or Me.txt_nazwa_instytucji = "" Then
        MsgBox "Proszê podaæ nazwê instytucji.", vbExclamation, "B³¹d"
        Me.txt_nazwa_instytucji.SetFocus
        Exit Sub
    End If
    
    If IsNull(Me.txt_ulica) Or Me.txt_ulica = "" Then
        MsgBox "Proszê podaæ ulicê.", vbExclamation, "B³¹d"
        Me.txt_ulica.SetFocus
        Exit Sub
    End If
    
    If IsNull(Me.txt_numer) Or Me.txt_numer = "" Then
        MsgBox "Proszê podaæ numer.", vbExclamation, "B³¹d"
        Me.txt_numer.SetFocus
        Exit Sub
    End If
    
    If IsNull(Me.txt_kod_pocztowy) Or Me.txt_kod_pocztowy = "" Then
        MsgBox "Proszê podaæ kod pocztowy.", vbExclamation, "B³¹d"
        Me.txt_kod_pocztowy.SetFocus
        Exit Sub
    End If
    
    If IsNull(Me.txt_miejscowosc) Or Me.txt_miejscowosc = "" Then
        MsgBox "Proszê podaæ miejscowoœæ.", vbExclamation, "B³¹d"
        Me.txt_miejscowosc.SetFocus
        Exit Sub
    End If
    
    If IsNull(Me.txt_miejsce_odbioru) Or Me.txt_miejsce_odbioru = "" Then
        MsgBox "Proszê podaæ miejsce odbioru.", vbExclamation, "B³¹d"
        Me.txt_miejsce_odbioru.SetFocus
        Exit Sub
    End If
    
    ' Sprawdzenie, czy miejsce odbioru jest inne niz adres
    If Me.txt_miejsce_odbioru <> "Adres" Then
        If IsNull(Me.txt_ulica_miejsca_odbioru) Or Me.txt_ulica_miejsca_odbioru = "" Then
            MsgBox "Proszê podaæ ulicê miejsca odbioru.", vbExclamation, "B³¹d"
            Me.txt_ulica_miejsca_odbioru.SetFocus
            Exit Sub
        End If
        'Sprawdzenie, czy wymagane pola miejsca odbioru s¹ wype³nione (jesli jest inne niz adres)
        If IsNull(Me.txt_numer_miejsca_odbioru) Or Me.txt_numer_miejsca_odbioru = "" Then
            MsgBox "Proszê podaæ numer miejsca odbioru.", vbExclamation, "B³¹d"
            Me.txt_numer_miejsca_odbioru.SetFocus
            Exit Sub
        End If
    
        If IsNull(Me.txt_kod_pocztowy_miejsca_odbioru) Or Me.txt_kod_pocztowy_miejsca_odbioru = "" Then
            MsgBox "Proszê podaæ kod pocztowy miejsca odbioru.", vbExclamation, "B³¹d"
            Me.txt_kod_pocztowy_miejsca_odbioru.SetFocus
            Exit Sub
        End If
    
        If IsNull(Me.txt_miejscowosc_miejsca_odbioru) Or Me.txt_miejscowosc_miejsca_odbioru = "" Then
            MsgBox "Proszê podaæ miejscowoœæ miejsca odbioru.", vbExclamation, "B³¹d"
            Me.txt_miejscowosc_miejsca_odbioru.SetFocus
            Exit Sub
        End If
    End If
    
    ' Przejscie do nowego rekordu
    DoCmd.GoToRecord , , acNewRec
    
    isSaveEnabled = True
    ' Zapis nowego rekordu
    If Me.Dirty Then
        Me.Dirty = False
    End If
    
    ' Wyswietlenie komunikatu o sukcesie
    MsgBox "Nowy klient zosta³ dodany.", vbInformation, "Sukces"
End Sub

Private Sub chk_takie_samo_miejsce_odbioru_AfterUpdate()
    If Me.chk_takie_samo_miejsce_odbioru.Value = True Then
        Me.txt_miejsce_odbioru.Value = "*Adres*"
    Else
        Me.txt_miejsce_odbioru.Value = ""
        Me.txt_ulica_miejsca_odbioru.Value = ""
        Me.txt_numer_miejsca_odbioru.Value = ""
        Me.txt_kod_pocztowy_miejsca_odbioru.Value = ""
        Me.txt_miejscowosc_miejsca_odbioru.Value = ""
    End If
    
    Me.txt_miejsce_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_ulica_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_numer_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_kod_pocztowy_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
    Me.txt_miejscowosc_miejsca_odbioru.Enabled = Not Me.chk_takie_samo_miejsce_odbioru.Value
End Sub

Private Sub Form_BeforeUpdate(Cancel As Integer)
    If Not isSaveEnabled Then
        Cancel = True
    End If
End Sub

Private Sub Form_Load()
    isSaveEnabled = False
    chk_takie_samo_miejsce_odbioru.Value = False
End Sub
