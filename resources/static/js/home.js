// ─── 自動補完＋カード検索＆クリックロジック ───
document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('.search-form');
  const input = form.querySelector('input[name="q"]');
  const cards = document.querySelectorAll('.card');

  // 1. すべてのカードタイトルを配列化
  const titles = Array.from(cards).map(card =>
    card.querySelector('h3').textContent
  );

  // 2. 自動補完用ドロップダウン（ul要素）を生成
  const suggest = document.createElement('ul');
  Object.assign(suggest.style, {
    position: 'absolute',
    top: `${input.offsetTop + input.offsetHeight}px`,
    left: `${input.offsetLeft}px`,
    width: `${input.offsetWidth}px`,
    border: '1px solid #ccc',
    background: '#fff',
    listStyle: 'none',
    margin: '0',
    padding: '0',
    maxHeight: '150px',
    overflowY: 'auto',
    zIndex: 1000,
  });
  // 親要素に relative を設定して絶対配置を有効化
  input.parentNode.style.position = 'relative';
  input.parentNode.appendChild(suggest);

  // 3. 入力ごとに補完候補を更新
  input.addEventListener('input', () => {
    const val = input.value.trim().toLowerCase();
    suggest.innerHTML = '';
    if (!val) {
      suggest.hidden = true;
      return;
    }
    // 前方一致
    const matches = titles.filter(t =>
      t.toLowerCase().startsWith(val)
    );
    matches.forEach(match => {
      const li = document.createElement('li');
      li.textContent = match;
      Object.assign(li.style, {
        padding: '4px 8px',
        cursor: 'pointer'
      });
      // 候補をクリックで入力フィールドに反映
      li.addEventListener('click', () => {
        input.value = match;
        suggest.hidden = true;
      });
      suggest.appendChild(li);
    });
    suggest.hidden = matches.length === 0;
  });

  // フォーカスが外れたら少し遅延して隠す
  input.addEventListener('blur', () => {
    setTimeout(() => suggest.hidden = true, 200);
  });

  // 4. フォーム送信時の検索動作
  form.addEventListener('submit', e => {
    e.preventDefault();  // ページリロードを防止

    const keyword = input.value.trim().toLowerCase();
    if (!keyword) return;

    // (A) 前方一致 or 部分一致で最初に見つかったカードをクリック
    let clicked = false;
    for (const card of cards) {
      const title = card.querySelector('h3').textContent.toLowerCase();
      if (title.includes(keyword)) {
        const link = card.querySelector('a');
        if (link) {
          link.click();
          clicked = true;
        }
        break;
      }
    }

    // (B) クリック遷移しなかったら、本ページで絞り込み表示
    if (!clicked) {
      cards.forEach(card => {
        const title = card.querySelector('h3').textContent.toLowerCase();
        card.style.display = title.includes(keyword) ? '' : 'none';
      });
    }
  });
});
