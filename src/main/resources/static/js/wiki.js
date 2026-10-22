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

const alvo = decodeURIComponent(location.hash.slice(1));
if (alvo) {
  const painel = document.getElementById(`panel-${alvo}`) ?? document.getElementById(alvo)?.closest(".wiki-panel");
  if (painel) abrirPainel(painel.id.replace("panel-", ""));
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
