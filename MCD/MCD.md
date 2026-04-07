@startuml
class Utilisateur {
    - id : Long
    - nom : String
    - prenom : String
    - email : String
    - motDePasse : String
    - isAdmin : boolean
    - isActif : boolean
}

class Canal {
    - id : Long
    - titre : String
    - description : String
    - dateHoraire : LocalDateTime
    - dureeValidite : int
}

class Message {
    - contenu : String
    - heure : LocalTime
}

' Relations liées à la gestion des canaux
Utilisateur "1" <--> "*" Canal : crée
Utilisateur "*" <--> "*" Canal : est invité à

' Relations liées à la diffusion en temps réel (WebSocket)
Message "*" --> "1" Canal : diffusé sur
Message "*" --> "1" Utilisateur : envoyé par
@enduml