const NAV = [
  { href: "/", label: "Início" },
  { href: "/personagens.html", label: "Personagens" },
  { href: "/racas.html", label: "Raças" },
  { href: "/classes.html", label: "Classes" },
  { href: "/armas.html", label: "Armas" },
  { href: "/armaduras.html", label: "Armaduras" },
  { href: "/escudos.html", label: "Escudos" },
  { href: "/divindades.html", label: "Divindades" },
  { href: "/pericias.html", label: "Perícias" },
  { href: "/feiticos.html", label: "Feitiços" },
  { href: "/inventarios.html", label: "Inventário" },
];

const CATALOGOS = [
  { href: "/personagens.html", endpoint: "/api/personagens", label: "Personagens", texto: "Ficha completa, atributos e escolhas." },
  { href: "/racas.html", endpoint: "/api/racas", label: "Raças", texto: "Nome, descrição e habilidade." },
  { href: "/classes.html", endpoint: "/api/classes", label: "Classes", texto: "Nome e descrição da classe." },
  { href: "/armas.html", endpoint: "/api/armas", label: "Armas", texto: "Ataque, nome e descrição." },
  { href: "/armaduras.html", endpoint: "/api/armaduras", label: "Armaduras", texto: "Defesa, nome e descrição." },
  { href: "/escudos.html", endpoint: "/api/escudos", label: "Escudos", texto: "Defesa, nome e descrição." },
  { href: "/divindades.html", endpoint: "/api/divindades", label: "Divindades", texto: "Nome e descrição." },
  { href: "/pericias.html", endpoint: "/api/pericias", label: "Perícias", texto: "Nome e descrição." },
  { href: "/feiticos.html", endpoint: "/api/feiticos", label: "Feitiços", texto: "Nome e descrição." },
  { href: "/inventarios.html", endpoint: "/api/inventarios", label: "Inventário", texto: "Itens e quantidades por personagem." },
];

function caminhoAtual() {
  const path = location.pathname;
  if (path === "/" || path.endsWith("/index.html")) return "/";
  return path;
}

function htmlNav() {
  const atual = caminhoAtual();
  const links = NAV.map((item) => {
    const ativo = item.href === atual;
    const classe = ativo
      ? "bg-amber-400 text-stone-950"
      : "bg-stone-900 text-stone-200 border border-stone-800";
    return `<a href="${item.href}" class="shrink-0 rounded-full px-3 py-1.5 text-sm font-medium ${classe}">${item.label}</a>`;
  }).join("");
  return `
    <header class="sticky top-0 z-20 border-b border-stone-800 bg-stone-950/95 backdrop-blur">
      <div class="mx-auto flex max-w-5xl items-center justify-between gap-3 px-4 py-3">
        <a href="/" class="font-serif text-lg tracking-wide text-amber-300">RpgServer</a>
      </div>
      <nav class="mx-auto flex max-w-5xl gap-2 overflow-x-auto px-4 pb-3" aria-label="Seções">${links}</nav>
    </header>`;
}

async function api(url, opcoes = {}) {
  const resposta = await fetch(url, {
    headers: { Accept: "application/json", ...(opcoes.body ? { "Content-Type": "application/json" } : {}) },
    ...opcoes,
  });
  const texto = await resposta.text();
  if (!resposta.ok) {
    let mensagem = texto || "Falha na requisição";
    try {
      const json = JSON.parse(texto);
      mensagem = json.detail || json.message || mensagem;
    } catch {
      /* corpo em texto puro */
    }
    throw new Error(mensagem);
  }
  if (!texto) return null;
  try {
    return JSON.parse(texto);
  } catch {
    return texto;
  }
}

function escapar(valor) {
  return String(valor ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;");
}

function lerJson(texto) {
  if (!texto) return null;
  try {
    return JSON.parse(texto);
  } catch {
    return null;
  }
}

function nomeRelacao(texto) {
  const objeto = lerJson(texto);
  return objeto?.nome || "";
}

function campoHtml(campo, valor) {
  const id = `campo-${campo.nome}`;
  const comum = "mt-1 w-full rounded-xl border border-stone-700 bg-stone-950 px-3 py-2 text-base text-stone-100 outline-none ring-amber-400/40 focus:ring-2";
  if (campo.tipo === "select") {
    const opcoes = (campo.opcoes || []).map((opcao) => {
      const selecionado = String(opcao.valor) === String(valor) ? "selected" : "";
      return `<option value="${escapar(opcao.valor)}" ${selecionado}>${escapar(opcao.label)}</option>`;
    }).join("");
    return `<label class="block text-sm text-stone-300" for="${id}">${campo.label}
      <select id="${id}" name="${campo.nome}" class="${comum}">${opcoes}</select>
    </label>`;
  }
  if (campo.tipo === "textarea") {
    return `<label class="block text-sm text-stone-300 sm:col-span-2" for="${id}">${campo.label}
      <textarea id="${id}" name="${campo.nome}" rows="3" class="${comum}">${escapar(valor)}</textarea>
    </label>`;
  }
  const limite = window.PAGINA?.limitePontos;
  if (campo.tipo === "number" && limite) {
    const numero = Number(valor || 0);
    return `<div class="rounded-xl border border-stone-800 bg-stone-950 px-3 py-3">
      <p class="text-sm text-stone-300">${campo.label}</p>
      <div class="mt-2 flex items-center justify-between gap-3">
        <button type="button" data-ajuste="-1" data-alvo="${id}" class="h-11 w-11 rounded-full border border-stone-700 text-xl leading-none disabled:opacity-30" aria-label="Diminuir ${campo.label}">−</button>
        <span data-exibir="${id}" class="font-serif text-2xl tabular-nums">${numero}</span>
        <button type="button" data-ajuste="1" data-alvo="${id}" class="h-11 w-11 rounded-full border border-stone-700 text-xl leading-none disabled:opacity-30" aria-label="Aumentar ${campo.label}">+</button>
      </div>
      <input id="${id}" name="${campo.nome}" type="hidden" data-pontos="1" value="${numero}" />
    </div>`;
  }
  const tipo = campo.tipo === "number" ? "number" : "text";
  const extra = campo.tipo === "number" ? 'min="0" inputmode="numeric"' : "";
  const obrigatorio = campo.obrigatorio ? "required" : "";
  return `<label class="block text-sm text-stone-300" for="${id}">${campo.label}
    <input id="${id}" name="${campo.nome}" type="${tipo}" ${extra} ${obrigatorio} value="${escapar(valor)}" class="${comum}" />
  </label>`;
}

function selectHtml(relacao, itens, selecionadoId) {
  const id = `campo-${relacao.nome}`;
  const opcoes = [`<option value="">Nenhuma</option>`]
    .concat(itens.map((item) => {
      const marcado = String(item.id) === String(selecionadoId) ? "selected" : "";
      return `<option value="${item.id}" ${marcado}>${escapar(item.nome || "Sem nome")}</option>`;
    }))
    .join("");
  return `<label class="block text-sm text-stone-300" for="${id}">${relacao.label}
    <select id="${id}" name="${relacao.nome}" class="mt-1 w-full rounded-xl border border-stone-700 bg-stone-950 px-3 py-2 text-base text-stone-100 outline-none ring-amber-400/40 focus:ring-2">${opcoes}</select>
  </label>`;
}

function initHome() {
  const app = document.getElementById("app");
  app.innerHTML = `
    ${htmlNav()}
    <main class="mx-auto max-w-5xl px-4 py-6">
      <h1 class="font-serif text-3xl text-amber-200 sm:text-4xl">Mesa do mestre</h1>
      <p class="mt-2 max-w-xl text-sm text-stone-400 sm:text-base">Cadastre raças, classes, equipamento e personagens. Tudo aqui fala com a API do servidor.</p>
      <section id="atalhos" class="mt-6 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3"></section>
    </main>`;

  const grade = document.getElementById("atalhos");
  grade.innerHTML = CATALOGOS.map((item) => `
    <a href="${item.href}" class="rounded-2xl border border-stone-800 bg-stone-900 p-4 transition hover:border-amber-400/60">
      <p class="text-xs uppercase tracking-wider text-amber-300/80" data-contagem="${item.endpoint}">carregando</p>
      <h2 class="mt-1 font-serif text-xl">${item.label}</h2>
      <p class="mt-1 text-sm text-stone-400">${item.texto}</p>
    </a>`).join("");

  CATALOGOS.forEach(async (item) => {
    const alvo = grade.querySelector(`[data-contagem="${item.endpoint}"]`);
    try {
      const lista = await api(item.endpoint);
      const total = Array.isArray(lista) ? lista.length : 0;
      alvo.textContent = total === 1 ? "1 registro" : `${total} registros`;
    } catch {
      alvo.textContent = "sem conexão";
    }
  });
}

async function initCrud() {
  const pagina = window.PAGINA;
  const app = document.getElementById("app");
  const estado = { lista: [], editando: null, catalogos: {} };

  app.innerHTML = `
    ${htmlNav()}
    <main class="mx-auto max-w-5xl px-4 py-6">
      <div class="flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="font-serif text-3xl text-amber-200">${pagina.titulo}</h1>
          <p class="text-sm text-stone-400">${pagina.descricao}</p>
        </div>
      </div>
      <p id="aviso" class="mt-4 hidden rounded-xl border px-3 py-2 text-sm" role="status"></p>
      <form id="formulario" class="mt-4 rounded-2xl border border-stone-800 bg-stone-900 p-4">
        <div class="mb-3 flex items-center justify-between gap-3">
          <h2 id="titulo-form" class="font-medium">Novo registro</h2>
          <button type="button" id="cancelar" class="hidden text-sm text-stone-400 underline">Cancelar edição</button>
        </div>
        <div id="pontos" class="mb-4 hidden text-center"></div>
        <div id="campos" class="grid grid-cols-1 gap-3 sm:grid-cols-2"></div>
        <button type="submit" id="salvar" class="mt-4 w-full rounded-xl bg-amber-400 px-4 py-3 font-semibold text-stone-950 sm:w-auto">Salvar</button>
      </form>
      <section class="mt-6">
        <h2 class="text-sm uppercase tracking-wider text-stone-500">Registros</h2>
        <div id="lista" class="mt-3 grid grid-cols-1 gap-3"></div>
      </section>
    </main>`;

  const aviso = document.getElementById("aviso");
  const campos = document.getElementById("campos");
  const listaEl = document.getElementById("lista");
  const form = document.getElementById("formulario");
  const tituloForm = document.getElementById("titulo-form");
  const cancelar = document.getElementById("cancelar");
  const pontosEl = document.getElementById("pontos");
  const botaoSalvar = document.getElementById("salvar");

  function mostrarAviso(texto, erro) {
    aviso.textContent = texto;
    aviso.className = `mt-4 rounded-xl border px-3 py-2 text-sm ${erro ? "border-red-900 bg-red-950 text-red-200" : "border-emerald-900 bg-emerald-950 text-emerald-200"}`;
  }

  function valorCampo(item, nome, padrao) {
    if (!item) return padrao ?? (nome.startsWith("valor") || ["forca", "inteligencia", "destreza", "sabedoria", "carisma"].includes(nome) ? 0 : "");
    return item[nome] ?? "";
  }

  function desenharFormulario(item) {
    const partes = pagina.campos.map((campo) => campoHtml(campo, valorCampo(item, campo.nome, campo.padrao)));
    (pagina.relacoes || []).forEach((relacao) => {
      const valorAtual = item?.[relacao.nome];
      const atual = relacao.idOnly ? valorAtual : lerJson(valorAtual)?.id;
      partes.push(selectHtml(relacao, estado.catalogos[relacao.endpoint] || [], atual ?? ""));
    });
    campos.innerHTML = partes.join("");
    tituloForm.textContent = item ? `Editando #${item.id}` : "Novo registro";
    cancelar.classList.toggle("hidden", !item);
    atualizarPontos();
  }

  function somaPontos() {
    if (!pagina.limitePontos) return 0;
    return [...form.querySelectorAll("[data-pontos]")].reduce((total, campo) => {
      const valor = Number(campo.value || 0);
      return total + (Number.isFinite(valor) ? valor : 0);
    }, 0);
  }

  function atualizarPontos() {
    if (!pagina.limitePontos) return;
    const soma = somaPontos();
    const restante = pagina.limitePontos - soma;
    const estourou = restante < 0;
    pontosEl.classList.remove("hidden");
    pontosEl.className = "mb-4 text-center";
    pontosEl.innerHTML = `
      <p class="font-serif text-5xl tabular-nums ${estourou ? "text-red-300" : "text-amber-200"}">${restante}</p>
      <p class="text-sm text-stone-400">pontos restantes</p>`;
    form.querySelectorAll("[data-pontos]").forEach((campo) => {
      const valor = Number(campo.value || 0);
      const exibir = form.querySelector(`[data-exibir="${campo.id}"]`);
      if (exibir) exibir.textContent = String(valor);
      const menos = form.querySelector(`[data-ajuste="-1"][data-alvo="${campo.id}"]`);
      const mais = form.querySelector(`[data-ajuste="1"][data-alvo="${campo.id}"]`);
      if (menos) menos.disabled = valor <= 0;
      if (mais) mais.disabled = restante <= 0;
    });
    botaoSalvar.disabled = estourou;
    botaoSalvar.classList.toggle("opacity-40", estourou);
  }

  function resumo(item) {
    const linhas = pagina.campos
      .filter((campo) => campo.nome !== "nome")
      .slice(0, 4)
      .map((campo) => `<p class="text-sm text-stone-400"><span class="text-stone-500">${campo.label}:</span> ${escapar(item[campo.nome])}</p>`);
    (pagina.relacoes || []).forEach((relacao) => {
      const nome = nomeRelacao(item[relacao.nome]);
      if (nome) linhas.push(`<p class="text-sm text-stone-400"><span class="text-stone-500">${relacao.label}:</span> ${escapar(nome)}</p>`);
    });
    return linhas.join("");
  }

  function desenharLista() {
    if (!estado.lista.length) {
      listaEl.innerHTML = `<p class="rounded-2xl border border-dashed border-stone-800 px-4 py-8 text-center text-sm text-stone-500">Nenhum registro ainda.</p>`;
      return;
    }
    listaEl.innerHTML = estado.lista.map((item) => `
      <article class="rounded-2xl border border-stone-800 bg-stone-900 p-4">
        <div class="flex items-start justify-between gap-3">
          <div>
            <p class="text-xs text-amber-300/80">#${item.id}</p>
            <h3 class="font-serif text-xl">${escapar(item.nome || "Sem nome")}</h3>
          </div>
        </div>
        <div class="mt-2 space-y-1">${resumo(item)}</div>
        <div class="mt-4 grid grid-cols-2 gap-2 sm:flex">
          <button type="button" data-acao="editar" data-id="${item.id}" class="rounded-xl border border-stone-700 px-3 py-2 text-sm">Editar</button>
          <button type="button" data-acao="excluir" data-id="${item.id}" class="rounded-xl border border-red-900 px-3 py-2 text-sm text-red-300">Excluir</button>
        </div>
      </article>`).join("");
  }

  function montarCorpo() {
    const dados = new FormData(form);
    const corpo = {};
    pagina.campos.forEach((campo) => {
      const bruto = dados.get(campo.nome);
      corpo[campo.nome] = campo.tipo === "number" ? Number(bruto || 0) : String(bruto ?? "");
    });
    (pagina.relacoes || []).forEach((relacao) => {
      const id = dados.get(relacao.nome);
      const itens = estado.catalogos[relacao.endpoint] || [];
      const escolhido = itens.find((item) => String(item.id) === String(id));
      corpo[relacao.nome] = escolhido
        ? (relacao.idOnly ? Number(escolhido.id) : JSON.stringify(escolhido))
        : null;
    });
    return corpo;
  }

  async function carregar() {
    try {
      if (pagina.relacoes) {
        await Promise.all(pagina.relacoes.map(async (relacao) => {
          estado.catalogos[relacao.endpoint] = await api(relacao.endpoint);
        }));
      }
      estado.lista = await api(pagina.endpoint);
      desenharFormulario(estado.editando);
      desenharLista();
    } catch (erro) {
      mostrarAviso(erro.message, true);
    }
  }

  form.addEventListener("click", (evento) => {
    const botao = evento.target.closest("[data-ajuste]");
    if (!botao || botao.disabled) return;
    const campo = document.getElementById(botao.dataset.alvo);
    if (!campo) return;
    const atual = Number(campo.value || 0);
    const passo = Number(botao.dataset.ajuste);
    const proximo = atual + passo;
    if (proximo < 0) return;
    if (passo > 0 && pagina.limitePontos && somaPontos() >= pagina.limitePontos) return;
    campo.value = String(proximo);
    atualizarPontos();
  });

  form.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    if (pagina.limitePontos && somaPontos() > pagina.limitePontos) {
      mostrarAviso(`A soma dos atributos não pode passar de ${pagina.limitePontos} pontos.`, true);
      return;
    }
    const corpo = montarCorpo();
    try {
      if (estado.editando) {
        await api(`${pagina.endpoint}/${estado.editando.id}`, { method: "PUT", body: JSON.stringify(corpo) });
        mostrarAviso("Registro atualizado.");
      } else {
        await api(pagina.endpoint, { method: "POST", body: JSON.stringify(corpo) });
        mostrarAviso("Registro criado.");
      }
      estado.editando = null;
      form.reset();
      await carregar();
    } catch (erro) {
      mostrarAviso(erro.message, true);
    }
  });

  cancelar.addEventListener("click", () => {
    estado.editando = null;
    form.reset();
    desenharFormulario(null);
  });

  listaEl.addEventListener("click", async (evento) => {
    const botao = evento.target.closest("button");
    if (!botao) return;
    const id = Number(botao.dataset.id);
    const item = estado.lista.find((registro) => registro.id === id);
    if (botao.dataset.acao === "editar" && item) {
      estado.editando = item;
      desenharFormulario(item);
      form.scrollIntoView({ behavior: "smooth", block: "start" });
    }
    if (botao.dataset.acao === "excluir") {
      if (!confirm("Excluir este registro?")) return;
      try {
        await api(`${pagina.endpoint}/${id}`, { method: "DELETE" });
        if (estado.editando?.id === id) {
          estado.editando = null;
        }
        mostrarAviso("Registro excluído.");
        await carregar();
      } catch (erro) {
        mostrarAviso(erro.message, true);
      }
    }
  });

  desenharFormulario(null);
  carregar();
}

if (window.PAGINA) {
  initCrud();
} else {
  initHome();
}
