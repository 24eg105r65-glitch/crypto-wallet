import React, { createContext, useContext, useState, useEffect } from 'react';

const VaultContext = createContext(null);

const DEFAULT_STATE = {
  balances: {
    BTC: { code: 'BTC', name: 'Bitcoin', standard: 'Native SegWit (P2WPKH)', balance: 48.25000000, priceUsd: 66000.00, icon: 'fab fa-bitcoin', color: '#F7931A', status: 'Air-Gapped', change7d: 4.10, address: 'bc1q9x4a2km89pz7u4c92k7s8d6a3v2e1w8z', cold: true },
    ETH: { code: 'ETH', name: 'Ethereum', standard: 'Institutional Validator Quorum', balance: 394.12000000, priceUsd: 3500.00, icon: 'fab fa-ethereum', color: '#627EEA', status: 'Compounding', apy: 3.85, change7d: 3.42, address: '0x71C9B98F4a298B321941F48C8120B88F098C12FA', cold: true },
    SOL: { code: 'SOL', name: 'Solana', standard: 'Institutional Timelock Stake', balance: 2285.00000000, priceUsd: 143.63, icon: 'fas fa-sun', color: '#14F195', status: 'Locked 14D', apy: 6.72, change7d: 1.20, address: '9xQe48KmPzWvB6mJ89A2kL4vT8C91a7E2vB3', cold: true },
    USDT: { code: 'USDT', name: 'Tether USD', standard: 'ERC-20 Liquid Rebalance Buffer', balance: 371801.15000000, priceUsd: 1.00, icon: 'fas fa-dollar-sign', color: '#26A17B', status: 'Liquid Buffer', change7d: 0.02, address: '0x89A11FE4928173489112948712CBA998F1234901', cold: false },
    XRP: { code: 'XRP', name: 'Ripple', standard: 'XRPL Cold Escrow', balance: 154000.00000000, priceUsd: 0.58, icon: 'fas fa-x', color: '#23292F', status: 'Air-Gapped', change7d: -0.85, address: 'rEb8TK3gBgk5auZyyb68Lkp54Dguq9V5A', cold: true }
  },
  transactions: [
    { id: 'tx-101', hash: '0x8a0cf35422774936b891e4a29810f', block: 18493481, asset: 'BTC', amount: '0.01000000', fee: '0.00012 BTC', status: 'CONFIRMED', time: '10 mins ago', type: 'DISPATCH' },
    { id: 'tx-102', hash: '0x33fd9012c9ee4b88912410a88712a', block: 18493412, asset: 'ETH', amount: '2.50000000', fee: '0.0018 ETH', status: 'CONFIRMED', time: '1 hour ago', type: 'INGRESS' },
    { id: 'tx-103', hash: '0x71cba889214710bc8921a4812399e', block: 18493390, asset: 'SOL', amount: '50.00000000', fee: '0.00005 SOL', status: 'CONFIRMED', time: '4 hours ago', type: 'DISPATCH' },
    { id: 'tx-104', hash: '0x99e120bc7714aa1928014bc912389', block: 18493200, asset: 'USDT', amount: '10000.00000000', fee: '1.50 USDT', status: 'CONFIRMED', time: 'Yesterday', type: 'INGRESS' }
  ]
};

export function VaultProvider({ children }) {
  const [state, setState] = useState(() => {
    try {
      const stored = localStorage.getItem('auravault_state_v1');
      if (stored) return JSON.parse(stored);
    } catch (e) {}
    return DEFAULT_STATE;
  });

  const [toasts, setToasts] = useState([]);
  const [emergencyFreeze, setEmergencyFreeze] = useState(false);

  useEffect(() => {
    try {
      localStorage.setItem('auravault_state_v1', JSON.stringify(state));
    } catch (e) {}
  }, [state]);

  const showToast = (message, type = 'gold') => {
    const id = Date.now() + Math.random();
    setToasts(prev => [...prev, { id, message, type }]);
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 4000);
  };

  const calculateValuation = () => {
    let totalUsd = 0;
    let coldUsd = 0;
    let liquidUsd = 0;

    for (const key in state.balances) {
      const item = state.balances[key];
      const val = item.balance * item.priceUsd;
      totalUsd += val;
      if (item.cold) coldUsd += val;
      else liquidUsd += val;
    }

    const chfRate = 0.89502;
    const inrRate = 83.50;
    const totalChf = totalUsd * chfRate;
    const totalInr = totalUsd * inrRate;
    const coldPct = totalUsd > 0 ? (coldUsd / totalUsd) * 100 : 92.4;
    const liquidPct = 100 - coldPct;

    return { totalUsd, totalChf, totalInr, coldUsd, liquidUsd, coldPct, liquidPct };
  };

  const sendAsset = (code, recipient, amount) => {
    if (emergencyFreeze) {
      showToast('ALERT: Capital dispatch rejected. Citadel Emergency Freeze active!', 'gold');
      return false;
    }
    const asset = state.balances[code];
    if (!asset || asset.balance < amount) {
      showToast(`Insufficient ${code} balance.`, 'gold');
      return false;
    }

    const newTx = {
      id: 'tx-' + Date.now(),
      hash: '0x' + Math.random().toString(16).substring(2, 10) + Math.random().toString(16).substring(2, 12),
      block: 18493482 + Math.floor(Math.random() * 20),
      asset: code,
      amount: amount.toFixed(6),
      fee: '0.00012 ' + code,
      status: 'CONFIRMED',
      time: 'Just now',
      type: 'DISPATCH'
    };

    setState(prev => ({
      ...prev,
      balances: {
        ...prev.balances,
        [code]: {
          ...prev.balances[code],
          balance: prev.balances[code].balance - amount
        }
      },
      transactions: [newTx, ...prev.transactions]
    }));

    showToast(`Dispatched ${amount} ${code}! Block #${newTx.block} confirmed.`, 'teal');
    return true;
  };

  const claimFaucet = (code, amount) => {
    const newTx = {
      id: 'faucet-' + Date.now(),
      hash: '0x' + Math.random().toString(16).substring(2, 10) + 'fauc',
      block: 18493485,
      asset: code,
      amount: '+' + amount.toFixed(6),
      fee: '0.00 ' + code,
      status: 'CONFIRMED',
      time: 'Just now',
      type: 'INGRESS'
    };

    setState(prev => ({
      ...prev,
      balances: {
        ...prev.balances,
        [code]: {
          ...prev.balances[code],
          balance: prev.balances[code].balance + amount
        }
      },
      transactions: [newTx, ...prev.transactions]
    }));

    showToast(`+${amount} ${code} credited from Testnet Dev Faucet!`, 'teal');
  };

  const resetState = () => {
    setState(DEFAULT_STATE);
    showToast('Swiss Alpine Vault baseline restored successfully.', 'teal');
  };

  const toggleFreeze = () => {
    setEmergencyFreeze(prev => {
      const next = !prev;
      if (next) {
        showToast('CRITICAL: Emergency Shard Isolation Engaged. All transfers locked.', 'gold');
      } else {
        showToast('Emergency Isolation lifted. Normal quorum rules restored.', 'teal');
      }
      return next;
    });
  };

  return (
    <VaultContext.Provider value={{
      balances: state.balances,
      transactions: state.transactions,
      valuation: calculateValuation(),
      sendAsset,
      claimFaucet,
      resetState,
      showToast,
      toasts,
      emergencyFreeze,
      toggleFreeze
    }}>
      {children}
      {/* Toast Notification Container */}
      <div className="fixed bottom-6 right-6 z-[9999] flex flex-col gap-2 pointer-events-none">
        {toasts.map(t => (
          <div
            key={t.id}
            className={`p-4 rounded-xl border backdrop-blur-xl shadow-2xl flex items-center gap-3 transition-all duration-300 pointer-events-auto max-w-md ${
              t.type === 'gold'
                ? 'bg-[#161B18]/95 border-[#79E6A0]/40 text-[#79E6A0]'
                : t.type === 'teal'
                ? 'bg-[#161B18]/95 border-[#14F195]/40 text-[#14F195]'
                : 'bg-[#161B18]/95 border-white/10 text-[#F5F7F5]'
            }`}
          >
            <span className="material-symbols-outlined text-[20px] shrink-0">
              {t.type === 'gold' ? 'verified' : t.type === 'teal' ? 'bolt' : 'info'}
            </span>
            <span className="text-sm font-medium text-[#F5F7F5]">{t.message}</span>
          </div>
        ))}
      </div>
    </VaultContext.Provider>
  );
}

export function useVault() {
  const context = useContext(VaultContext);
  if (!context) {
    throw new Error('useVault must be used within a VaultProvider');
  }
  return context;
}
