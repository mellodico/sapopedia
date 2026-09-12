const ACCOUNT_KEY = "sapopedia-conta";
const SESSION_KEY = "sapopedia-sessao";

function getAccount() {
  try {
    return JSON.parse(localStorage.getItem(ACCOUNT_KEY)) || null;
  } catch (error) {
    return null;
  }
}

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
  counter.textContent = `0/${maxLength}`;
  container.appendChild(counter);

  textarea.addEventListener("input", () => {
    const count = textarea.value.length;
    counter.textContent = `${count}/${maxLength}`;
    counter.classList.toggle("is-near-limit", count > maxLength * 0.8);
  });
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

function initializeAuth() {
  const loginForm = document.querySelector('[data-form="login"]');
  const registerForm = document.querySelector('[data-form="registro"]');
  const accountForm = document.querySelector('[data-form="conta"]');

  if (loginForm) {
    const emailField = loginForm.email;
    const message = loginForm.querySelector(".form-message");

    emailField.addEventListener("input", () => {
      const isValid = validateEmail(emailField.value);
      emailField.classList.toggle(
        "is-invalid",
        !isValid && emailField.value.length > 0,
      );
      emailField.classList.toggle("is-valid", isValid);
    });

    loginForm.addEventListener("submit", (event) => {
      event.preventDefault();
      const account = getAccount();
      const email = loginForm.email.value.trim().toLowerCase();
      const password = loginForm.senha.value;

      if (
        !account ||
        account.email !== email ||
        account.password !== password
      ) {
        showMessage(message, "E-mail ou senha incorretos.", true);
        return;
      }

      localStorage.setItem(SESSION_KEY, "true");
      window.location.href = "/conta";
    });
  }

  if (registerForm) {
    const emailField = registerForm.email;
    const passwordField = registerForm.senha;
    const confirmationField = registerForm.confirmacao;
    const message = registerForm.querySelector(".form-message");

    emailField.addEventListener("input", () => {
      const isValid = validateEmail(emailField.value);
      emailField.classList.toggle(
        "is-invalid",
        !isValid && emailField.value.length > 0,
      );
      emailField.classList.toggle("is-valid", isValid);
    });

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
      event.preventDefault();
      const password = registerForm.senha.value;
      const confirmation = registerForm.confirmacao.value;

      if (password !== confirmation) {
        showMessage(message, "As senhas precisam ser iguais.", true);
        return;
      }

      const account = {
        nome: registerForm.nome.value.trim(),
        usuario: registerForm.usuario.value.trim(),
        email: registerForm.email.value.trim().toLowerCase(),
        bio: "",
        password,
      };
      localStorage.setItem(ACCOUNT_KEY, JSON.stringify(account));
      localStorage.setItem(SESSION_KEY, "true");
      window.location.href = "/conta";
    });
  }

  if (accountForm) {
    const account = getAccount();
    const loggedIn = localStorage.getItem(SESSION_KEY) === "true";
    if (!account || !loggedIn) {
      window.location.href = "/login";
      return;
    }

    accountForm.nome.value = account.nome;
    accountForm.usuario.value = account.usuario;
    accountForm.email.value = account.email;
    accountForm.bio.value = account.bio || "";

    const bioField = accountForm.querySelector('[name="bio"]');
    if (bioField) {
      addCharacterCounter(bioField, 240);
    }

    const emailField = accountForm.email;
    emailField.addEventListener("input", () => {
      const isValid = validateEmail(emailField.value);
      emailField.classList.toggle(
        "is-invalid",
        !isValid && emailField.value.length > 0,
      );
      emailField.classList.toggle("is-valid", isValid);
    });

    accountForm.addEventListener("submit", (event) => {
      event.preventDefault();
      const updatedAccount = {
        ...account,
        nome: accountForm.nome.value.trim(),
        usuario: accountForm.usuario.value.trim(),
        email: accountForm.email.value.trim().toLowerCase(),
        bio: accountForm.bio.value.trim(),
      };
      localStorage.setItem(ACCOUNT_KEY, JSON.stringify(updatedAccount));
      showMessage(
        accountForm.querySelector(".form-message"),
        "Dados atualizados com sucesso.",
      );
    });

    document
      .querySelector('[data-action="logout"]')
      ?.addEventListener("click", () => {
        if (confirm("Tem certeza que deseja sair da conta?")) {
          localStorage.removeItem(SESSION_KEY);
          window.location.href = "/login";
        }
      });
  }
}

function initializePage() {
  initializeAuth();
  initializeMessages();
}

document.addEventListener("DOMContentLoaded", initializePage);
