Feature: Login API

  @smoke
  Scenario: Login API berhasil dengan kredensial valid
    Given client telah mengambil captcha dari API
    When client mengirim request login API dengan username "admin1" dan password valid
    Then response API login memiliki status code 200
    And response API login berstatus "SUCCESS" dan memiliki data token

  @negatif
  Scenario: Login API gagal dengan password salah
    Given client telah mengambil captcha dari API
    When client mengirim request login API dengan username "admin1" dan password yang salah
    Then response API login memiliki status code 401
    And response API login berstatus "ERROR" dengan error code "API-ECMXS40107"
