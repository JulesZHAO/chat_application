import React, { useEffect, useState, useRef} from "react";
import { Client } from '@stomp/stompjs'
import { useParams} from "react-router-dom";

function Chat() {
    const { canalId } = useParams();
    const [utilisateur, setUtilisateur] = useState("");
    const [messages, setMessages] = useState([]);
    const [utilisateursConnectes, setUtilisateursConnectes] = useState([]);
    const [inputMessage, setInputMessage] = useState("");
    const [titreCan, setTitreCanal] = useState("");
    const clientRef = useRef();

    useEffect(() => {
        fetch("/moi").then(res => res.json()).then(u => setUtilisateur(u.nom));
        fetch(`/canaux/${canalId}`).then(res => res.json()).then(c => setTitreCanal(c.titre));
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

    const messagesEndRef = useRef();

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

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

    const formatHeure = (heure) => {
        if (!heure) return '';
        return heure.substring(0, 5);
    };

    return (
        <div style={{ display: 'flex', height: '75vh', background: '#f5f5f7', fontFamily: 'SF Pro Text, system-ui, -apple-system, sans-serif', border: '1px solid #e0e0e0', borderRadius: '18px', overflow: 'hidden' }}>

            {/* Fil de discussion */}
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>

                {/* Header */}
                <div style={{ padding: '16px 20px', background: '#ffffff', borderBottom: '1px solid #e0e0e0' }}>
                    <h2 style={{ margin: 0, fontSize: '17px', fontWeight: '600', color: '#1d1d1f' }}>{titreCan}</h2>
                    <span style={{ fontSize: '12px', color: '#7a7a7a' }}>{utilisateursConnectes.length} connecté{utilisateursConnectes.length > 1 ? 's' : ''}</span>
                </div>

                {/* Messages */}
                <div style={{ flex: 1, overflowY: 'auto', padding: '20px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {messages.map((m, i) => {
                        const estMoi = m.expediteur === utilisateur;
                        return (
                            <div key={i} style={{ display: 'flex', flexDirection: 'column', alignItems: estMoi ? 'flex-end' : 'flex-start' }}>
                                {!estMoi && (
                                    <span style={{ fontSize: '12px', color: '#7a7a7a', marginBottom: '4px', marginLeft: '4px' }}>{m.expediteur}</span>
                                )}
                                <div style={{
                                    maxWidth: '60%',
                                    padding: '10px 14px',
                                    borderRadius: estMoi ? '18px 18px 4px 18px' : '18px 18px 18px 4px',
                                    background: estMoi ? '#0066cc' : '#ffffff',
                                    color: estMoi ? '#ffffff' : '#1d1d1f',
                                    fontSize: '15px',
                                    lineHeight: '1.4',
                                    border: estMoi ? 'none' : '1px solid #e0e0e0'
                                }}>
                                    {m.contenu}
                                </div>
                                <span style={{ fontSize: '11px', color: '#7a7a7a', marginTop: '4px', marginLeft: '4px', marginRight: '4px' }}>
                                    {formatHeure(m.heure)}
                                </span>
                            </div>
                        );
                    })}
                    <div ref={messagesEndRef} />
                </div>

                {/* Input */}
                <div style={{ padding: '12px 16px', background: '#ffffff', borderTop: '1px solid #e0e0e0', display: 'flex', gap: '10px', alignItems: 'center' }}>
                    <input
                        type="text"
                        aria-label="Écrire un message"
                        style={{ flex: 1, padding: '10px 16px', borderRadius: '9999px', border: '1px solid #e0e0e0', fontSize: '15px', outline: 'none', background: '#f5f5f7', color: '#1d1d1f' }}
                        value={inputMessage}
                        onChange={e => setInputMessage(e.target.value)}
                        onKeyDown={e => e.key === 'Enter' && envoyerMessage()}
                        placeholder="Écrire un message..."
                    />
                    <button className="btn-primary" onClick={envoyerMessage}>Envoyer</button>
                </div>
            </div>

            {/* Liste des utilisateurs connectés */}
            <div style={{ width: '220px', background: '#ffffff', borderLeft: '1px solid #e0e0e0', display: 'flex', flexDirection: 'column' }}>
                <div style={{ padding: '16px 20px', borderBottom: '1px solid #e0e0e0' }}>
                    <h3 style={{ margin: 0, fontSize: '14px', fontWeight: '600', color: '#7a7a7a', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                        Connectés — {utilisateursConnectes.length}
                    </h3>
                </div>
                <div style={{ padding: '12px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    {utilisateursConnectes.map((u, i) => (
                        <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '8px 10px', borderRadius: '10px', background: '#f5f5f7' }}>
                            <div style={{ width: '32px', height: '32px', borderRadius: '9999px', background: '#0066cc', color: '#ffffff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '13px', fontWeight: '600', flexShrink: 0 }}>
                                {u.charAt(0).toUpperCase()}
                            </div>
                            <span style={{ fontSize: '14px', color: '#1d1d1f', fontWeight: '500' }}>{u}</span>
                        </div>
                    ))}
                </div>
            </div>

        </div>
    );
}

export default Chat;