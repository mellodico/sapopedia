function showMessage(element, message, isError = false) {
  if (!element) return;
  element.textContent = message;
  element.classList.toggle("is-error", isError);
  element.classList.add("is-visible");
}

function validateEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

function addCharacterCounter(textarea, maxLength) {
  const container = textarea.parentElement;
  const counter = document.createElement("div");
  counter.className = "char-counter";
  container.appendChild(counter);

  const updateCounter = () => {
    const count = textarea.value.length;
    counter.textContent = `${count}/${maxLength}`;
    counter.classList.toggle("is-near-limit", count > maxLength * 0.8);
  };
  textarea.addEventListener("input", updateCounter);
  updateCounter();
}

function initializeMessages() {
  const messagesForm = document.querySelector('[data-form="mensagens"]');
  if (!messagesForm) return;

  const titleField = messagesForm.titulo;
  const messageField = messagesForm.mensagem;
  const feedback = messagesForm.querySelector(".form-message");

  [titleField, messageField].forEach((field) => {
    field.addEventListener("input", () => {
      field.classList.remove("is-invalid");
    });
  });

  messagesForm.addEventListener("submit", (event) => {
    const hasTitle = titleField.value.trim().length > 0;
    const hasMessage = messageField.value.trim().length > 0;

    titleField.classList.toggle("is-invalid", !hasTitle);
    messageField.classList.toggle("is-invalid", !hasMessage);

    if (!hasTitle && !hasMessage) {
      event.preventDefault();
      showMessage(feedback, "Preencha o título e escreva uma mensagem.", true);
    }
  });
}

function addEmailValidation(emailField) {
  emailField.addEventListener("input", () => {
    const isValid = validateEmail(emailField.value);
    emailField.classList.toggle(
      "is-invalid",
      !isValid && emailField.value.length > 0,
    );
    emailField.classList.toggle("is-valid", isValid);
  });
}

function initializeAuth() {
  const loginForm = document.querySelector('[data-form="login"]');
  const registerForm = document.querySelector('[data-form="registro"]');
  const accountForm = document.querySelector('[data-form="conta"]');

  // Versões antigas guardavam a conta (com senha) no navegador
  localStorage.removeItem("sapopedia-conta");
  localStorage.removeItem("sapopedia-sessao");

  if (loginForm) {
    addEmailValidation(loginForm.email);
  }

  if (registerForm) {
    const passwordField = registerForm.senha;
    const confirmationField = registerForm.confirmacao;
    const message = registerForm.querySelector(".form-message");

    addEmailValidation(registerForm.email);

    passwordField.addEventListener("input", () => {
      if (confirmationField.value) {
        const match = passwordField.value === confirmationField.value;
        confirmationField.classList.toggle("is-invalid", !match);
        confirmationField.classList.toggle("is-valid", match);
      }
    });

    confirmationField.addEventListener("input", () => {
      const match = passwordField.value === confirmationField.value;
      confirmationField.classList.toggle(
        "is-invalid",
        !match && confirmationField.value.length > 0,
      );
      confirmationField.classList.toggle(
        "is-valid",
        match && confirmationField.value.length > 0,
      );
    });

    registerForm.addEventListener("submit", (event) => {
      if (passwordField.value !== confirmationField.value) {
        event.preventDefault();
        showMessage(message, "As senhas precisam ser iguais.", true);
      }
    });
  }

  if (accountForm) {
    addCharacterCounter(accountForm.bio, 240);
    addEmailValidation(accountForm.email);
  }

  document
    .querySelector('form[data-action="logout"]')
    ?.addEventListener("submit", (event) => {
      if (!confirm("Tem certeza que deseja sair da conta?")) {
        event.preventDefault();
      }
    });
}

function initializePage() {
  initializeAuth();
  initializeMessages();
}

document.addEventListener("DOMContentLoaded", initializePage);
