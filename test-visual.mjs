import { execSync } from 'child_process';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

console.log('========================================================');
console.log('🚀 ANTIGRAVITY PREMIUM UI/UX VISUAL AUDIT HARNESS');
console.log('========================================================');

const targetPath = path.resolve(__dirname, 'frontend/index.html').replace(/\\/g, '/');

console.log(`\n📸 [1/2] Capturing Desktop Viewport (1440x900)...`);
execSync(`npx playwright screenshot --channel chrome --viewport-size "1440,900" "${targetPath}" "${path.resolve(__dirname, 'visual-desktop.png')}"`, { stdio: 'inherit' });
console.log(`✔ Saved: visual-desktop.png`);

console.log(`\n📸 [2/2] Capturing Mobile Viewport (375x812)...`);
execSync(`npx playwright screenshot --channel chrome --viewport-size "375,812" "${targetPath}" "${path.resolve(__dirname, 'visual-mobile.png')}"`, { stdio: 'inherit' });
console.log(`✔ Saved: visual-mobile.png`);

console.log('\n========================================================');
console.log('✅ AUDIT COMPLETE: All viewports rendered successfully!');
console.log('========================================================\n');
