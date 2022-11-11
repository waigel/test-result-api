/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./pages/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'site-image': "url('/images/background.png')",
      },
      colors: {
        brand: {
          500: '#1c1d22'
        },
        primary: {
          500: '#103178',
          600: '#242424'
        }
      }
    },
  },
  plugins: [],
}