Feature: Lupa Password API

  @smoke
  Scenario: Lupa password API berhasil dengan email terdaftar
    Given client telah mengambil captcha dari API
    When client mengirim request lupa password API dengan email "admin1@simpleapps.test"
    Then response API lupa password memiliki status code 200
    And response API lupa password berstatus "SUCCESS" dan memiliki data magic link

  @negatif
  Scenario: Lupa password API gagal karena captcha salah
    When client mengirim request lupa password API dengan email "admin1@simpleapps.test" dan captcha tidak valid
    Then response API lupa password memiliki status code 400
    And response API lupa password berstatus "ERROR" dengan error code "API-ECM010022"
