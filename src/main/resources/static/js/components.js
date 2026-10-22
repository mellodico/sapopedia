const NAV_ITEMS = [
  {
    key: "inicio",
    label: "Início",
    href: "/",
    icon: "home-svgrepo-com.svg",
  },
  {
    key: "wiki",
    label: "Wiki",
    href: "/wiki",
    icon: "book-open-svgrepo-com.svg",
  },
  {
    key: "imagens",
    label: "Imagens",
    href: "/imagens",
    icon: "image-filled-svgrepo-com.svg",
  },
  {
    key: "mensagens",
    label: "Mensagens",
    href: "/mensagens",
    icon: "message-svgrepo-com.svg",
  },
];

class SiteHeader extends HTMLElement {
  connectedCallback() {
    const active = this.getAttribute("active");
    const usuario = this.getAttribute("usuario");
    const navHtml = NAV_ITEMS.map(
        (item) => `
      <a class="nav-item${item.key === active ? " active" : ""}" href="${item.href}">
        <img class="icon-img" src="/img/${item.icon}" alt="">
        ${item.label}
      </a>
    `,
    ).join("");

    this.innerHTML = `
      <header class="header">
        <div class="logo-frog"><img src="/img/frog(2).png" alt="Logo SapoPédia"></div>
        <div class="brand">
          <h1>SAPOPÉDIA</h1>
          <p>A WIKI E COMUNIDADE DOS ANUROS BRASILEIROS</p>
        </div>
        <div class="header-actions">
          ${usuario ? `
          <a class="btn btn-outline" href="/conta">
            <img class="icon-img" src="/img/person-fill-svgrepo-com.svg" alt="">
            ${usuario}
          </a>
          <form action="/logout" method="post">
            <button class="btn btn-solid" type="submit">
              <img class="icon-img icon-invert" src="/img/enter-outline-svgrepo-com.svg" alt="">
              Sair
            </button>
          </form>
          ` : `
          <a class="btn btn-outline" href="/login">
            <img class="icon-img" src="/img/enter-outline-svgrepo-com.svg" alt="">
            Entrar
          </a>
          <a class="btn btn-solid" href="/registro">
            <img class="icon-img icon-invert" src="/img/person-fill-svgrepo-com.svg" alt="">
            Criar conta
          </a>
          `}
        </div>
      </header>

      <nav class="nav" aria-label="Navegação principal">
        <ul class="nav-list">
          ${navHtml}
        </ul>
      </nav>
    `;
  }
}

class SiteFooter extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <footer class="footer">
        <div class="footer-brand">
          <div class="logo-frog"><img src="/img/frog(2).png" alt="Logo SapoPédia"></div>
          <div>
            <h2>SapoPédia</h2>
            <p>Wiki colaborativa dos anuros brasileiros</p>
          </div>
        </div>
        <p class="footer-credit">Trabalho de Programação IV &middot; 2026</p>
      </footer>
    `;
  }
}

customElements.define("site-header", SiteHeader);
customElements.define("site-footer", SiteFooter);