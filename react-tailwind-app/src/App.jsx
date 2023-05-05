import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="App">
      <div class="w-screen">
        <div class="bg-black h-10 text-white font-light text-2xl text-center">
          Scroll Down!
        </div>

        <div class="h-32 bg-red-600"></div>
        <div class="h-32 bg-green-600"></div>
        <div class="h-32 bg-white"></div>

        <div class="h-32 bg-red-500"></div>
        <div class="h-32 bg-green-500"></div>
        <div class="h-32 bg-white"></div>

        <div class="h-32 bg-red-400"></div>
        <div class="h-32 bg-green-400"></div>
        <div class="h-32 bg-white"></div>

        <div class="h-32 bg-red-300"></div>
        <div class="h-32 bg-green-300"></div>
        <div class="h-32 bg-white"></div>

        <div class="h-32 bg-red-200"></div>
        <div class="h-32 bg-green-200"></div>
        <div class="h-32 bg-white"></div>

        <div class="h-32 bg-red-100"></div>
        <div class="h-32 bg-green-100"></div>
        <div class="h-32 bg-white"></div>
      </div>
    </div>
  )
}

export default App
