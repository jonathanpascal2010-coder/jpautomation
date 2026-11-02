Feature: Registrasi API

  @smoke
  Scenario: Registrasi API berhasil dengan seluruh data valid
    Given client telah mengambil captcha dari API
    When client mengirim request registrasi API dengan seluruh data valid dan field terenkripsi
    Then response API registrasi memiliki status code 200
    And response API registrasi berstatus "SUCCESS" dan memiliki data magic link

  @negatif
  Scenario: Registrasi API gagal dengan nomor KTP bukan 16 digit
    Given client telah mengambil captcha dari API
    When client mengirim request registrasi API dengan nomor KTP tidak 16 digit
    Then response API registrasi memiliki status code 400
    And response API registrasi berstatus "ERROR" dengan error code "API-ECM010001" dan field error "API-ECM010009"
