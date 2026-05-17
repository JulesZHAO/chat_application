import React, { useEffect, useState, useRef} from "react";
import { Client } from '@stomp/stompjs'
import { useParams} from "react-router-dom";

function Chat() {
    const { canalId } = useParams();
    const [utilisateur, setUtilisateur] = useState("");
    const [messages, setMessages] = useState([]);
    const [utilisateursConnectes, setUtilisateursConnectes] = useState([]);
    const [inputMessage, setInputMessage] = useState("");
    const clientRef = useRef();

    useEffect(() => {
        fetch("/moi").then(res => res.json()).then(u => setUtilisateur(u.nom));
    }, []);

    useEffect(() => {
        if(!utilisateur) return;

        const client = new Client({
            brokerURL: 'ws://localhost:8080/chat-ws',
            onConnect: () => {
                // Recevoir les messages du canal
                client.subscribe(`/topic/canal/${canalId}`, (frame) => {
                    const msg = JSON.parse(frame.body);
                    setMessages(prev => [...prev, msg]);
                });

                // Recevoir la liste des utilisateurs connectes
                client.subscribe(`/topic/canal/${canalId}/utilisateurs`, (frame) => {
                    const users = JSON.parse(frame.body);
                    setUtilisateursConnectes(users);
                });

                // Annoncer sa connexion
                client.publish({
                    destination: `/app/canal/${canalId}/rejoindre`,
                    body: JSON.stringify({ expediteur: utilisateur})
                });
            },
        });

        client.activate();
        clientRef.current = client;

        return () => {
            // Annocer sa deconnnextion
            if (clientRef.current?.connected) {
                clientRef.current.publish({
                    destination: `/app/canal/${canalId}/quitter`,
                    body: JSON.stringify({ expediteur: utilisateur })
                });
            }
            client.deactivate();
        };
    }, [canalId, utilisateur]);

    const envoyerMessage = () => {
        if (!inputMessage.trim()) return;
        clientRef.current.publish({
            destination: `/app/canal/${canalId}/send`,
            body: JSON.stringify({
                contenu: inputMessage,
                expediteur: utilisateur
            })
        });
        setInputMessage("");
    };

    return (
        <div style={{ display: 'flex', gap: '20px', padding: '20px' }}>

            {/* Fil de discussion */}
            <div style={{ flex: 3 }}>
                <h5>Discussion</h5>
                <textarea
                    readOnly
                    rows={20}
                    style={{ width: '100%' }}
                    value={messages.map(m => `[${m.heure}] ${m.expediteur} : ${m.contenu}`).join('\n')}
                />
                <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                    <input
                        type="text"
                        style={{ flex: 1 }}
                        value={inputMessage}
                        onChange={e => setInputMessage(e.target.value)}
                        onKeyDown={e => e.key === 'Enter' && envoyerMessage()}
                        placeholder="Écrire un message..."
                    />
                    <button onClick={envoyerMessage}>Envoyer</button>
                </div>
            </div>

            {/* Liste des utilisateurs connectés */}
            <div style={{ flex: 1 }}>
                <h5>Connectés ({utilisateursConnectes.length})</h5>
                <ul>
                    {utilisateursConnectes.map((u, i) => (
                        <li key={i}>{u}</li>
                    ))}
                </ul>
            </div>

        </div>
    );
}

export default Chat;