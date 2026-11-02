Feature: Registrasi Web

  @smoke
  Scenario: Registrasi akun baru berhasil dengan seluruh data valid
    Given user berada di halaman registrasi
    When user mengisi formulir registrasi dengan data valid
    And user memilih jenis kelamin "Pria" dan tanggal lahir tahun 2020 Januari 15
    And user mengisi captcha registrasi dan menekan tombol submit
    Then browser diarahkan ke halaman verifikasi check email

  @negatif
  Scenario: Registrasi akun baru gagal karena nomor KTP tidak 16 digit
    Given user berada di halaman registrasi
    When user mengisi formulir registrasi dengan nomor KTP tidak 16 digit
    And user memilih jenis kelamin "Pria" dan tanggal lahir tahun 2020 Januari 15
    And user mengisi captcha registrasi dan menekan tombol submit
    Then form registrasi menolak pengiriman dan tetap di halaman registrasi
