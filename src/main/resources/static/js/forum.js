(function () {
  const list = document.querySelector("#forum-list");
  if (!list) return;

  const empty = document.querySelector("#forum-empty");
  const count = document.querySelector("#forum-count");
  const searchInput = document.querySelector("#forum-search");
  const filterSelect = document.querySelector("#forum-filter-tipo");

  function renderPosts() {
    const termo = searchInput.value.trim().toLowerCase();
    const tipo = filterSelect.value;
    const posts = [...list.querySelectorAll(".forum-post")];

    let encontrados = 0;
    posts.forEach((post) => {
      const titulo = post.dataset.titulo.toLowerCase();
      const corpo = post.dataset.corpo.toLowerCase();
      const postTipo = post.dataset.tipo;
      const bateBusca =
        !termo ||
        titulo.includes(termo) ||
        corpo.includes(termo);
      const bateTipo = tipo === "todos" || postTipo === tipo;
      const visivel = bateBusca && bateTipo;

      post.hidden = !visivel;
      if (visivel) encontrados += 1;
    });

    empty.hidden = encontrados > 0;
    count.textContent = `${encontrados} ${encontrados === 1 ? "post encontrado" : "posts encontrados"}`;
  }

  searchInput.addEventListener("input", renderPosts);
  filterSelect.addEventListener("change", renderPosts);

  renderPosts();
})();