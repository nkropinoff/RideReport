$(document).ready(function () {
   const $newPassword = $('#newPassword');
   const $confirmPassword = $('#confirmPassword');
   const $passwordForm = $(`form[action='${ctx}/profile/password']`);
   const $feedback = $passwordForm.find('.invalid-feedback');

   function validatePasswords() {
      if ($confirmPassword.val() === '') {
         $confirmPassword.removeClass('is-invalid');
         $feedback.hide();
         return;
      }

      if ($newPassword.val() !== $confirmPassword.val()) {
         $confirmPassword.addClass('is-invalid');
         $feedback.show();
      } else {
         $confirmPassword.removeClass('is-invalid');
         $feedback.hide();
      }
   }

   $newPassword.on('blur', validatePasswords);
   $confirmPassword.on('blur', validatePasswords);

   $passwordForm.on('submit', function(e) {
      if ($newPassword.val() !== $confirmPassword.val()) {
         e.preventDefault();
         $confirmPassword.addClass('is-invalid');
         $feedback.show();
      }
   })
});