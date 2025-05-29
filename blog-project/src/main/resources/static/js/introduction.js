// ページの読み込みが完了したら中の処理を開始します
document.addEventListener('DOMContentLoaded', () => {
  // ── 1. カードの3D傾きをマウス位置に合わせて動かす ──
  document.querySelectorAll('.card').forEach(card => {
    // カード内でマウスが動いたら、回転量を計算してCSS変数にセット
    card.addEventListener('pointermove', e => {
      const rect = card.getBoundingClientRect();
      const x = (e.clientX - rect.left) / rect.width  - 0.5;
      const y = (e.clientY - rect.top ) / rect.height - 0.5;
      card.style.setProperty('--rx', `${x * 15}deg`);
      card.style.setProperty('--ry', `${-y * 15}deg`);
    });
    // マウスがカードから離れたら、回転をリセット
    card.addEventListener('pointerleave', () => {
      card.style.setProperty('--rx', '0deg');
      card.style.setProperty('--ry', '0deg');
    });
  });

  // ── 2. マウスホイールで一画面ずつスナップスクロール ──
  const container = document.querySelector('.page-container');
  const sections = Array.from(container.querySelectorAll('section'));
  let isScrolling = false; // スクロール制御用フラグ

  // ホイール操作をキャッチして、次のセクションへ移動
  container.addEventListener('wheel', e => {
    e.preventDefault();            // デフォルトのスクロールを停止
    if (isScrolling) return;      // 連続実行を防ぐ
    isScrolling = true;

    // 今表示中のセクションのインデックスを取得
    const idx = sections.findIndex(sec => {
      const top = sec.getBoundingClientRect().top;
      return top >= -sec.clientHeight / 2;
    });

    // スクロール方向を判定（deltaY > 0 なら下へ）
    const dir = e.deltaY > 0 ? 1 : -1;
    // 範囲内にクランプして次のインデックスを決定
    const targetIdx = Math.min(Math.max(idx + dir, 0), sections.length - 1);

    // 滑らかにそのセクションへ移動
    sections[targetIdx].scrollIntoView({ behavior: 'smooth' });

    // 0.8秒後にロック解除
    setTimeout(() => isScrolling = false, 800);
  }, { passive: false });

  // ── 3. 「トップに戻る」ボタン制御 ──
  const backBtn = document.getElementById('backToTop');

  // スクロール位置に応じてボタン表示/非表示を切り替える
  window.addEventListener('scroll', () => {
    if (window.scrollY > window.innerHeight) {
      backBtn.classList.add('show');
    } else {
      backBtn.classList.remove('show');
    }
  });

  // ボタンクリックでトップへスムーズスクロール
  backBtn.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });

  // ── 4. サイドナビのドットハイライト ──
  const dots = document.querySelectorAll('.nav-dot');

  // IntersectionObserverを使って、半分以上見えたらアクティブにする
  const io = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        dots.forEach(d => d.classList.remove('active'));
        const id = entry.target.id;
        const activeDot = document.querySelector(`.side-nav a[href="#${id}"]`);
        if (activeDot) activeDot.classList.add('active');
      }
    });
  }, { threshold: 0.5 });

  // 全セクションを監視対象に登録
  sections.forEach(sec => io.observe(sec));
});