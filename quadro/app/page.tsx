"use client"
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import Link from "next/link"
import TitleRoute from "@/components/titleRoute"
import * as lucide from "lucide-react"
import { useState } from "react"

export default function index() {

  const [isVisible, setIsVisible] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  const handleLogin = () => {
    if (!email || !password) {
      alert("Preencha todos os campos")
      return
    }

    const url = "http://localhost:3005/token"

    const response = fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    })
      .then(res => res.json())
      .then(data => {
        if (data.token) {
          localStorage.setItem("token", data.token)
          window.location.href = "/home"
        } else {
          alert(data.message)
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  const toggleVisibility = () => {
    setIsVisible(true)
  }
  return (
    <div>
      <TitleRoute title="Logar-se" />
      <div className="flex items-center justify-center px-4 md:px-0">
                <Card className="w-full max-w-[500px] p-4 md:p-0">
          <CardHeader>
            <CardTitle>Olá de volta</CardTitle>
            <CardDescription>Acesse o quadro</CardDescription>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="space-y-1">
              <Label htmlFor="email">Email</Label>
              <Input id="email" onChange={(e) => setEmail(e.target.value)} placeholder="Email qual usou para se cadastrar" />
            </div>
            <div className="space-y-1 relative">
              <Label htmlFor="password">Senha</Label>
            </div>
            <div className="relative">
              <Input
                onChange={(e) => setPassword(e.target.value)}
                type={isVisible ? "text" : "password"}
                id="password"
                placeholder="Sua senha"
                className="pr-10"
              />
              <div
                onClick={toggleVisibility}
                className="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer"
              >
                {isVisible ? (
                  <lucide.EyeOff className="w-5 h-5 text-gray-500" />
                ) : (
                  <lucide.Eye className="w-5 h-5 text-gray-500" />
                )}
              </div>
            </div>
          </CardContent>
          <CardFooter>
            <Link className="text-sm text-muted-foreground" href="/"> Esqueceu sua senha?</Link>
          </CardFooter>
          <CardFooter>
            <Button onClick={handleLogin} className="w-full">
              Entrar
            </Button>
          </CardFooter>
        </Card>
      </div>
    </div>
  )
}