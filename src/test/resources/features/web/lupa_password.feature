Feature: Lupa Password Web

  @smoke
  Scenario: Permintaan lupa password berhasil untuk email terdaftar
    Given user berada di halaman lupa password
    When user mengajukan lupa password dengan email terdaftar "admin1@simpleapps.test"
    Then browser diarahkan ke halaman instruksi check email

  @negatif
  Scenario: Permintaan lupa password gagal untuk email tidak terdaftar
    Given user berada di halaman lupa password
    When user mengajukan lupa password dengan email tidak terdaftar "tidak_terdaftar_9999@example.com"
    Then muncul notifikasi error atau user tetap berada di halaman lupa password
