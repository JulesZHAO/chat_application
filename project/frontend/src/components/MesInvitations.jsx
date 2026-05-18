import { useEffect, useState} from "react";

function MesInvitations() {
    const [invitations, setInvitations] = useState([]);
    const [userId, setUserId] = useState(null);
    const [pageCourante, setPageCourante] = useState(0);
    const parPage = 3;
    const debut = pageCourante * parPage;
    const invitationsDelaPage = invitations.slice(debut, debut + parPage);

    useEffect(() => {
        fetch("/moi").then(res => res.json()).then(u => setUserId(u.id))
    }, []);

    useEffect(() => {
        if (!userId) return;
        fetch(`/canaux/invite/${userId}`)
            .then(res => res.json())
            .then(data => setInvitations(data))
    }, [userId])
    return (
        <div className="page-content">
            <h2>Mes invitations</h2>

            {invitations.length === 0 ? (
                <p>Aucune invitation pour le moment.</p>
            ) : (
                invitationsDelaPage.map(invitation => (
                    <div key={invitation.id} className="canal-card">
                        <div>
                            <h3>{invitation.titre}</h3>
                            <p>{invitation.description}</p>
                        </div>
                        <div className="canal-card-actions">
                            <button className="btn-primary" onClick={() => window.open(`/chat/${invitation.id}`, '_blank')}>
                                Rejoindre
                            </button>
                        </div>
                    </div>
                ))
            )}
            {(() => {
                const totalPages = Math.max(1, Math.ceil(invitations.length / parPage));
                const TAILLE = 26;
                const GAP = 3;
                const PADDING = 3;
                const debut5 = Math.max(0, Math.min(pageCourante - 2, totalPages - 5));
                const fin5 = Math.min(totalPages, debut5 + 5);
                const pages = Array.from({ length: fin5 - debut5 }, (_, i) => debut5 + i);
                const indicateurPos = pageCourante - debut5;
                return (
                    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                        <div style={{ position: 'relative', display: 'inline-flex', alignItems: 'center', gap: `${GAP}px`, background: '#f0f0f0', borderRadius: '9999px', padding: `${PADDING}px` }}>
                            <div style={{
                                position: 'absolute',
                                background: '#0066cc',
                                borderRadius: '9999px',
                                width: `${TAILLE}px`,
                                height: `${TAILLE}px`,
                                transition: 'transform 0.25s cubic-bezier(0.4, 0, 0.2, 1)',
                                transform: `translateX(${indicateurPos * (TAILLE + GAP)}px)`,
                                left: `${PADDING}px`,
                                pointerEvents: 'none'
                            }} />
                            {pages.map(i => (
                                <button key={i} onClick={() => setPageCourante(i)} style={{
                                    position: 'relative',
                                    zIndex: 1,
                                    width: `${TAILLE}px`,
                                    height: `${TAILLE}px`,
                                    border: 'none',
                                    background: 'transparent',
                                    borderRadius: '9999px',
                                    cursor: 'pointer',
                                    color: i === pageCourante ? '#ffffff' : '#1d1d1f',
                                    fontSize: '14px',
                                    fontWeight: i === pageCourante ? '600' : '400',
                                    transition: 'color 0.25s ease',
                                    padding: 0
                                }}>
                                    {i + 1}
                                </button>
                            ))}
                        </div>
                    </div>
                );
            })()}
        </div>
    )
}

export default MesInvitations;