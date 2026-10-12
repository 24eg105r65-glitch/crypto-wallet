import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

const DEFAULT_USER = {
  username: 'pushkaran',
  fullName: 'Adabala Pushkaran',
  email: 'pushkaran@cryptowallet.io',
  role: 'ROLE_ADMIN',
  initials: 'AP',
  id: '24EG105R65',
  tier: 'Private Banking Client Tier 1'
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('cryptowallet_session');
      if (stored) {
        const parsed = JSON.parse(stored);
        const nameParts = (parsed.fullName || parsed.username || 'AP').trim().split(' ');
        const initials = nameParts.length > 1 
          ? (nameParts[0][0] + nameParts[1][0]).toUpperCase() 
          : nameParts[0].substring(0, 2).toUpperCase();
        return {
          ...DEFAULT_USER,
          ...parsed,
          initials
        };
      }
    } catch (e) {
      console.error('Error loading session:', e);
    }
    return DEFAULT_USER;
  });

  const login = (username, password) => {
    const isPushkaran = username.toLowerCase() === 'pushkaran' || username.toLowerCase() === 'admin';
    const fullName = isPushkaran ? 'Adabala Pushkaran' : username;
    const nameParts = fullName.trim().split(' ');
    const initials = nameParts.length > 1 
      ? (nameParts[0][0] + nameParts[1][0]).toUpperCase() 
      : nameParts[0].substring(0, 2).toUpperCase();

    const sessionData = {
      username,
      fullName,
      email: username.includes('@') ? username : `${username}@cryptowallet.io`,
      role: username.toLowerCase() === 'admin' ? 'ROLE_ADMIN' : 'ROLE_USER',
      id: '24EG105R65',
      tier: 'Private Banking Client Tier 1',
      initials,
      token: 'cw_jwt_' + Math.random().toString(36).substring(2) + Date.now(),
      timestamp: new Date().toISOString()
    };

    localStorage.setItem('cryptowallet_session', JSON.stringify(sessionData));
    setUser(sessionData);
    return sessionData;
  };

  const register = (userData) => {
    const fullName = userData.fullName || userData.username;
    const nameParts = fullName.trim().split(' ');
    const initials = nameParts.length > 1 
      ? (nameParts[0][0] + nameParts[1][0]).toUpperCase() 
      : nameParts[0].substring(0, 2).toUpperCase();

    const sessionData = {
      ...userData,
      id: '24EG105R65',
      tier: 'Private Banking Client Tier 1',
      initials,
      role: 'ROLE_USER',
      token: 'cw_jwt_' + Math.random().toString(36).substring(2) + Date.now(),
      timestamp: new Date().toISOString()
    };

    localStorage.setItem('cryptowallet_session', JSON.stringify(sessionData));
    setUser(sessionData);
    return sessionData;
  };

  const logout = () => {
    try {
      localStorage.removeItem('cryptowallet_session');
      sessionStorage.clear();
    } catch (err) {
      console.error(err);
    }
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
