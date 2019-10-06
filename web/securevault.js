var pubkey =
`-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6NxvZHf6eBzmIvfvRAOZ
UHPL8pzY5xdrFd0qa5Gh/E215tKFQ2vMMBpF/yyA2KE55bwaQnUPNkzPxPKV5MCL
rqdobV/HO6F4m4XIDP2PA6sJUmMjhh8X6aAzQ1rgMyF+J0z6zGY2kh2LtBAGDnu5
wfY+cORY/CyJZ7y8RRxEdeTDtsVnRe/xz++9cIF6e+yYqwJLa+nHD894oFbVlSok
NJh8e2eqpkIvfVotmp4JTjDJp9bpH+ibHWi3gj/o3SXvu832LHn1d5fANB9sQ44r
UjDfhr8h0bA8ZkO5Hj9W39M5WJK9MqzgV5lgb3patN0wOosPOKRBRKdA65jRbuxo
pwIDAQAB
-----END PUBLIC KEY-----
`

$(document).ready(function() {

  $("#challenge").submit(function(event) {
    event.preventDefault();
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey($('#pubkey').val());

    email = $('#email').val();
    passwd = $('#passwd').val();
    jsonlogin = {
      "email": email,
      "passwd": passwd
    }

    var encrypted = encrypt.encrypt(JSON.stringify(jsonlogin));
    $.post("/login", {
      encrypted: encrypted
    }, function(data) {
      $('#content').text(data)
      $('#msg_modal').on('shown.bs.modal', function() {}).modal('show');
    });
  })
});
