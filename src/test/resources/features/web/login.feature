Feature: Login Web

  @smoke
  Scenario: Login berhasil dengan kredensial valid
    Given user berada di halaman login
    When user melakukan login dengan username "admin1" dan password "Admin1@123"
    Then browser diarahkan ke halaman dashboard
    And nama user yang login menampilkan "Admin Satu"

  @negatif
  Scenario: Login gagal dengan password salah
    Given user berada di halaman login
    When user melakukan login dengan username "admin1" dan password "PasswordSalah123!"
    Then muncul notifikasi error pada halaman login
    And user tetap berada di halaman login
