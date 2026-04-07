@startuml
class Utilisateur {
    - id : int
    - nom : String
    - prenom : String
    - email : String
    - motDePasse : String
    - isAdmin : boolean
    - isActif : boolean
    + getChatsCrees() : List<Chat>
    + getChatsInvites() : List<Chat>
}

class Chat {
    - id : int
    - titre : String
    - description : String
    - dateHoraire : LocalDateTime
    - dureeValidite : int
    + getProprietaire() : Utilisateur
    + getInvites() : List<Utilisateur>
}
' Relations
Utilisateur "1" --> "*" Chat : crée
Utilisateur "*" --> "*" Chat : est invité à
@enduml