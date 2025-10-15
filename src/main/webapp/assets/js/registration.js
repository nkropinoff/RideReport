document.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    const role = params.get("role");

    if (role === 'company') {
        const companyTab = document.getElementById('company-tab');
        if (companyTab) companyTab.click();
    }
});