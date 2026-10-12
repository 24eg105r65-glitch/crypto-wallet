import React, { useState } from 'react';
import { useVault } from '../context/VaultContext';

export default function SendModal({ isOpen, onClose, initialAsset = 'BTC' }) {
  const { balances, sendAsset, showToast } = useVault();
  const [selectedAsset, setSelectedAsset] = useState(initialAsset);
  const [recipient, setRecipient] = useState('');
  const [amount, setAmount] = useState('');

  if (!isOpen) return null;

  const currentAsset = balances[selectedAsset] || balances['BTC'];

  const handleSubmit = (e) => {
    e.preventDefault();
    const num = parseFloat(amount);
    if (!num || num <= 0) {
      showToast('Please enter a valid amount.', 'gold');
      return;
    }
    const success = sendAsset(selectedAsset, recipient, num);
    if (success) {
      setAmount('');
      setRecipient('');
      onClose();
    }
  };

  const handleMax = () => {
    if (currentAsset) {
      setAmount(currentAsset.balance.toString());
    }
  };

  const handleAlice = () => {
    setRecipient('bc1qalice9x82km89pz7u4c92k7s8d6a3v2e1w8z');
    showToast('Pasted verified trustee recipient address.', 'teal');
  };

  const gasFees = {
    BTC: '0.00012 BTC ($7.92)',
    ETH: '0.0018 stETH ($6.30)',
    SOL: '0.00005 SOL ($0.01)',
    USDT: '1.50 USDT ($1.50)',
    XRP: '0.02 XRP ($0.01)'
  };

  return (
    <div 
      className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fadeIn"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
    >
      <div className="bg-[#161B18] border border-white/10 max-w-lg w-full p-6 sm:p-8 rounded-2xl shadow-2xl flex flex-col gap-5 relative">
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/25 flex items-center justify-center text-[#79E6A0]">
              <span className="material-symbols-outlined text-[20px]">send</span>
            </div>
            <div>
              <h3 className="font-display font-bold text-lg text-[#F5F7F5]">Initiate Capital Dispatch</h3>
              <p className="text-xs text-[#A0AAA3] font-mono">Air-Gapped Multi-Sig Transfer</p>
            </div>
          </div>
          <button 
            type="button" 
            onClick={onClose}
            className="text-[#707B73] hover:text-[#F5F7F5] transition-colors p-1"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <label className="text-xs text-[#A0AAA3] uppercase font-mono tracking-wider">Select Reserve Asset</label>
            <select
              value={selectedAsset}
              onChange={(e) => setSelectedAsset(e.target.value)}
              className="w-full bg-[#111513] border border-white/10 text-[#F5F7F5] p-3 rounded-xl font-mono text-sm focus:border-[#79E6A0] focus:outline-none"
            >
              <option value="BTC">Bitcoin (BTC) — Native SegWit</option>
              <option value="ETH">Ethereum (stETH) — Validator Pool</option>
              <option value="SOL">Solana (SOL) — Timelock Safe</option>
              <option value="USDT">Tether USD (USDT) — ERC-20 Buffer</option>
              <option value="XRP">Ripple (XRP) — Cold Escrow</option>
            </select>
            <div className="flex justify-between items-center text-xs text-[#A0AAA3] font-mono pt-0.5">
              <span>Available Vault Holdings:</span>
              <span className="text-[#79E6A0] font-semibold">{currentAsset.balance.toLocaleString()} {currentAsset.code}</span>
            </div>
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-xs text-[#A0AAA3] uppercase font-mono tracking-wider">Recipient Custodial Address / Shard</label>
            <div className="relative">
              <input
                type="text"
                required
                value={recipient}
                onChange={(e) => setRecipient(e.target.value)}
                placeholder="e.g. bc1qalice4x92km89pz7u4c92k7s8d6a3v2e1w8z"
                className="w-full bg-[#111513] border border-white/10 text-[#F5F7F5] p-3 rounded-xl font-mono text-xs focus:border-[#79E6A0] focus:outline-none pr-28"
              />
              <button
                type="button"
                onClick={handleAlice}
                className="absolute right-2 top-2 bg-[#1F2622] hover:bg-[#28322c] text-[#79E6A0] text-[10px] font-mono font-bold px-2.5 py-1.5 rounded-lg border border-[#79E6A0]/20 transition-colors"
              >
                PASTE ALICE
              </button>
            </div>
          </div>

          <div className="flex flex-col gap-1.5">
            <div className="flex justify-between items-center">
              <label className="text-xs text-[#A0AAA3] uppercase font-mono tracking-wider">Transfer Amount</label>
              <button
                type="button"
                onClick={handleMax}
                className="text-xs text-[#79E6A0] font-mono hover:underline font-bold"
              >
                MAX ALLOCATION
              </button>
            </div>
            <input
              type="number"
              step="any"
              min="0.000001"
              required
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="0.00"
              className="w-full bg-[#111513] border border-white/10 text-[#F5F7F5] p-3 rounded-xl font-mono text-lg font-bold focus:border-[#79E6A0] focus:outline-none"
            />
          </div>

          <div className="bg-[#111513] p-3.5 rounded-xl border border-white/5 flex flex-col gap-2 text-xs font-mono">
            <div className="flex justify-between">
              <span className="text-[#A0AAA3]">Estimated Network Gas Fee:</span>
              <span className="text-[#F5F7F5]">{gasFees[selectedAsset] || '0.0001 ' + selectedAsset}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-[#A0AAA3]">Consensus Requirement:</span>
              <span className="text-[#79E6A0] font-bold">4 of 7 Trustees</span>
            </div>
          </div>

          <div className="flex gap-3 pt-2">
            <button
              type="submit"
              className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold py-3 rounded-xl transition-all shadow-md active:translate-y-px"
            >
              Formulate Transfer Packet
            </button>
            <button
              type="button"
              onClick={onClose}
              className="w-full bg-[#1F2622] hover:bg-[#28322c] text-[#F5F7F5] font-display py-3 rounded-xl border border-white/10 transition-colors"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
