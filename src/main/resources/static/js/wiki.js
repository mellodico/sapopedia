const navLinks = document.querySelectorAll("[data-panel]");
const panels = document.querySelectorAll(".wiki-panel");

function abrirPainel(nome) {
  panels.forEach((p) => {
    p.classList.toggle("is-active", p.id === `panel-${nome}`);
  });

  document.querySelectorAll(".wiki-sidebar__nav a").forEach((link) => {
    link.classList.toggle("active", link.dataset.panel === nome);
  });
}

navLinks.forEach((el) => {
  el.addEventListener("click", (event) => {
    event.preventDefault();
    abrirPainel(el.dataset.panel);
  });
});

const searchInput = document.getElementById("wiki-search");
const searchablePanels = document.querySelectorAll(
  "#panel-especies, #panel-familias, #panel-generos, #panel-glossario",
);

if (searchInput) {
  searchInput.addEventListener("input", () => {
    const termo = searchInput.value.trim().toLowerCase();
    let primeiroPainelComResultado = null;

    searchablePanels.forEach((panel) => {
      let algumVisivelNoPainel = false;

      panel.querySelectorAll(".alpha-group").forEach((grupo) => {
        const links = grupo.querySelectorAll(".wiki-index-link");
        let algumVisivelNoGrupo = false;

        links.forEach((link) => {
          const texto = link.textContent.trim().toLowerCase();
          const combina = termo === "" || texto.includes(termo);
          link.classList.toggle("no-match", !combina);
          if (combina) algumVisivelNoGrupo = true;
        });

        grupo.classList.toggle("no-match", !algumVisivelNoGrupo);
        if (algumVisivelNoGrupo) algumVisivelNoPainel = true;
      });

      panel.querySelectorAll(".wiki-table tbody tr").forEach((row) => {
        const combina =
          termo === "" || row.textContent.toLowerCase().includes(termo);
        row.classList.toggle("no-match", !combina);
        if (combina) algumVisivelNoPainel = true;
      });

      if (algumVisivelNoPainel && termo !== "" && !primeiroPainelComResultado) {
        primeiroPainelComResultado = panel.id.replace("panel-", "");
      }
    });

    if (termo !== "" && primeiroPainelComResultado) {
      abrirPainel(primeiroPainelComResultado);
    }
  });
}
