// Slidev 53 on Vite 8: the default theme's code-block CSS trips the lightningcss
// minifier. The deck is served locally, so minified CSS buys nothing.
export default {
  build: { cssMinify: false },
}
