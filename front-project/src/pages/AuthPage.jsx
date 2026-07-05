import { useState, useEffect } from 'react';
import styled, { keyframes } from 'styled-components';
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { Button } from '../components/ui/Button';
import { Input, Label, FormGroup, Select } from '../components/ui/Input';
import { Modal } from '../components/ui/Modal';
import { HeartPulse, Stethoscope, UserRound, Database } from 'lucide-react';
import { authService, clientService, doctorService } from '../services/api';
import { PROCEDURE_TYPES } from '../config/constants';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../components/ui/LanguageSwitcher';
import { parseApiError } from '../utils/errorHandler';

const fadeIn = keyframes`
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
`;

const AuthContainer = styled.div`
  display: flex;
  min-height: 100vh;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle at top right, ${({ theme }) => theme.colors.surface}, ${({ theme }) => theme.colors.background});
  padding: 2rem;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -10%;
    right: -5%;
    width: 50vw;
    height: 50vw;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(99, 102, 241, 0.15) 0%, transparent 70%);
    z-index: 0;
  }
`;

const GlassCard = styled.div`
  background: ${({ theme }) => theme.colors.glass};
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 3rem;
  width: 100%;
  max-width: 500px;
  box-shadow: ${({ theme }) => theme.shadows.card};
  z-index: 1;
  animation: ${fadeIn} 0.6s ease-out;
`;

const Header = styled.div`
  text-align: center;
  margin-bottom: 2.5rem;
`;

const Logo = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  font-size: 2rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
  margin-bottom: 1rem;
`;

const Title = styled.h2`
  font-size: 1.5rem;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: 0.5rem;
`;

const Subtitle = styled.p`
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 0.95rem;
`;

const ToggleGroup = styled.div`
  display: flex;
  background: ${({ theme }) => theme.colors.background};
  border-radius: ${({ theme }) => theme.radii.md};
  padding: 0.25rem;
  margin-bottom: 2rem;
`;

const ToggleButton = styled.button`
  flex: 1;
  padding: 0.75rem;
  border-radius: ${({ theme }) => theme.radii.sm};
  background: ${({ active, theme }) => active ? theme.colors.surfaceHover : 'transparent'};
  color: ${({ active, theme }) => active ? theme.colors.text : theme.colors.textMuted};
  font-weight: 600;
  transition: ${({ theme }) => theme.transitions.default};
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;

  &:hover {
    color: ${({ theme }) => theme.colors.text};
  }
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const Row = styled.div`
  display: flex;
  gap: 1rem;
  width: 100%;
`;

const SwitchMode = styled.div`
  text-align: center;
  margin-top: 1.5rem;
  color: ${({ theme }) => theme.colors.textMuted};
  font-size: 0.9rem;
  
  span {
    color: ${({ theme }) => theme.colors.primary};
    cursor: pointer;
    font-weight: 600;
    transition: ${({ theme }) => theme.transitions.default};
    
    &:hover {
      text-decoration: underline;
    }
  }
`;

const ErrorMsg = styled.div`
  color: ${({ theme }) => theme.colors.danger};
  font-size: 0.9rem;
  text-align: center;
  margin-bottom: 1rem;
`;

const AuthPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();
  const [role, setRole] = useState('patient');
  const [mode, setMode] = useState('login');
  const [isSent, setIsSent] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [modalConfig, setModalConfig] = useState({ isOpen: false, title: '', message: '', type: 'info' });

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const initialMode = params.get('mode');
    if (initialMode === 'register' || initialMode === 'login') {
      setMode(initialMode);
    }
  }, [location]);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    phoneNumber: '',
    sex: 'MALE',
    email: '',
    password: '',
    specialization: '',
    professionalDescription: '',
    stateCertificateNumber: ''
  });

  const handleInputChange = (e) => {
    let { name, value } = e.target;
    // Strip spaces from phone number so it's strictly digits for the backend
    if (name === 'phoneNumber') {
      value = value.replace(/[^0-9]/g, '');
    }
    setFormData({ ...formData, [name]: value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    setLoading(true);
    try {
      if (mode === 'login') {
        const response = await authService.login({ username: formData.email, password: formData.password });
        const { accessToken } = response.data;
        
        // Save token so interceptor uses it
        localStorage.setItem('token', accessToken);
        
        // Decode to find role
        const decoded = jwtDecode(accessToken);
        const roles = decoded.realm_access?.roles || [];
        
        if (roles.includes('dpasystem.ADMIN')) {
          localStorage.setItem('user', JSON.stringify({ role: 'admin', email: formData.email }));
          navigate('/admin/dashboard');
        } else if (roles.includes('dpasystem.CLIENT')) {
          const profileRes = await clientService.getMe();
          localStorage.setItem('user', JSON.stringify({ role: 'patient', ...profileRes.data }));
          navigate('/patient/dashboard');
        } else if (roles.includes('dpasystem.DOCTOR') || roles.includes('dpasystem.DOCTOR_PENDING')) {
          const profileRes = await doctorService.getMe();
          const verificationStatus = roles.includes('dpasystem.DOCTOR_PENDING') ? 'PENDING' : 'APPROVED';
          localStorage.setItem('user', JSON.stringify({ role: 'doctor', verificationStatus, ...profileRes.data }));
          navigate('/doctor/dashboard'); // Or wherever doctor goes
        } else {
          setError('User role not recognized.');
          localStorage.removeItem('token');
        }
      } else {
        // Register Mode
        const payloadWithPhone = { ...formData, phoneNumber: '+995' + formData.phoneNumber };
        if (role === 'doctor') {
          await authService.registerDoctor(payloadWithPhone);
          setIsSent(true);
        } else {
          // Patient register
          const { specialization, professionalDescription, stateCertificateNumber, ...patientPayload } = payloadWithPhone;
          await authService.registerClient(patientPayload);
          setMode('login');
          setModalConfig({
            isOpen: true,
            title: 'Registration Successful',
            message: 'Your account has been created. Please log in to continue.',
            type: 'success'
          });
        }
      }
    } catch (err) {
      console.error(err);
      
      let errorMsg = parseApiError(err);
      
      // Override specific messages for auth context if needed
      if (errorMsg.includes('This resource (e.g. Email, Phone, Certificate) is already registered')) {
        errorMsg = 'This account cannot be created because the Email, Phone Number, or Certificate is already registered in our system.';
      }

      setError(errorMsg);
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    } finally {
      setLoading(false);
    }
  };

  if (isSent) {
    return (
      <AuthContainer>
        <div style={{ position: 'absolute', top: '2rem', right: '2rem', zIndex: 10 }}>
          <LanguageSwitcher />
        </div>
        <GlassCard style={{ textAlign: 'center' }}>
          <Stethoscope size={64} color="#10b981" style={{ margin: '0 auto 1.5rem' }} />
          <Title>{t('auth.pendingTitle')}</Title>
          <Subtitle style={{ marginBottom: '2rem' }}>
            {t('auth.pendingDesc')}
          </Subtitle>
          <Button fullWidth onClick={() => { setIsSent(false); setMode('login'); }}>
            {t('auth.backToLogin')}
          </Button>
        </GlassCard>
      </AuthContainer>
    );
  }

  return (
    <AuthContainer>
      <div style={{ position: 'absolute', top: '2rem', right: '2rem', zIndex: 10 }}>
        <LanguageSwitcher />
      </div>

      <GlassCard>
        <Header>
          <Logo onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
            <HeartPulse size={36} />
            HealthBridge
          </Logo>
          <Title>{t('auth.welcome')}</Title>
          <Subtitle>{t('auth.subtitle')}</Subtitle>
        </Header>

        {mode === 'register' && (
          <ToggleGroup>
            <ToggleButton active={role === 'patient'} onClick={() => setRole('patient')} type="button">
              <UserRound size={18} /> {t('auth.patientAccess')}
            </ToggleButton>
            <ToggleButton active={role === 'doctor'} onClick={() => setRole('doctor')} type="button">
              <Stethoscope size={18} /> {t('auth.doctorPortal')}
            </ToggleButton>
          </ToggleGroup>
        )}

        <Form onSubmit={handleSubmit}>
          {error && <ErrorMsg>{error}</ErrorMsg>}

          {mode === 'register' && (
            <>
              <Row>
                <FormGroup>
                  <Label>{t('auth.firstName')}</Label>
                  <Input required name="firstName" value={formData.firstName} onChange={handleInputChange} placeholder="John" />
                </FormGroup>
                <FormGroup>
                  <Label>{t('auth.lastName')}</Label>
                  <Input required name="lastName" value={formData.lastName} onChange={handleInputChange} placeholder="Doe" />
                </FormGroup>
              </Row>
              <Row>
                <FormGroup>
                  <Label>{t('auth.dob')}</Label>
                  <Input required type="date" name="dateOfBirth" max={new Date().toISOString().split('T')[0]} value={formData.dateOfBirth} onChange={handleInputChange} />
                </FormGroup>
                <FormGroup>
                  <Label>Sex</Label>
                  <Select name="sex" value={formData.sex} onChange={handleInputChange}>
                    <option value="MALE">{t('auth.male')}</option>
                    <option value="FEMALE">{t('auth.female')}</option>
                  </Select>
                </FormGroup>
              </Row>
              <FormGroup>
                <Label>{t('auth.phone')}</Label>
                <div style={{ display: 'flex' }}>
                  <div style={{ 
                    display: 'flex', 
                    alignItems: 'center', 
                    padding: '0 1rem', 
                    background: '#f1f5f9', 
                    border: '1px solid #cbd5e1', 
                    borderRight: 'none', 
                    borderRadius: '0.5rem 0 0 0.5rem', 
                    color: '#64748b',
                    fontWeight: '600'
                  }}>
                    +995
                  </div>
                  <Input 
                    required 
                    type="tel" 
                    name="phoneNumber" 
                    pattern="[0-9]{9}"
                    title="Phone number must be exactly 9 digits"
                    value={formData.phoneNumber} 
                    onChange={handleInputChange} 
                    placeholder="555123456" 
                    style={{ borderRadius: '0 0.5rem 0.5rem 0', flex: 1 }} 
                  />
                </div>
              </FormGroup>
              
              {role === 'doctor' && (
                <>
                  <FormGroup>
                    <Label>{t('auth.specialization')}</Label>
                    <Select required name="specialization" value={formData.specialization} onChange={handleInputChange}>
                      <option value="" disabled>{t('auth.specialization')}</option>
                      {PROCEDURE_TYPES.map(p => (
                        <option key={p.category} value={p.category}>{p.category}</option>
                      ))}
                    </Select>
                  </FormGroup>
                  <FormGroup>
                    <Label>{t('auth.certNumber')}</Label>
                    <Input required name="stateCertificateNumber" value={formData.stateCertificateNumber} onChange={handleInputChange} placeholder="CERT-12345" />
                  </FormGroup>
                  <FormGroup>
                    <Label>{t('auth.bio')} (Max 255 chars)</Label>
                    <Input required name="professionalDescription" value={formData.professionalDescription} maxLength={255} onChange={handleInputChange} placeholder="Brief bio..." />
                  </FormGroup>
                </>
              )}
            </>
          )}

          <FormGroup>
            <Label>{t('auth.email')}</Label>
            <Input required type="email" name="email" value={formData.email} onChange={handleInputChange} placeholder="you@example.com" />
          </FormGroup>
          
          <FormGroup>
            <Label>{t('auth.password')}</Label>
            <Input required type="password" name="password" minLength={8} maxLength={72} value={formData.password} onChange={handleInputChange} />
          </FormGroup>

          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Processing...' : (mode === 'login' ? t('auth.signInBtn') : (role === 'doctor' ? 'Submit Request' : t('auth.createAccountBtn')))}
          </Button>
        </Form>

        <SwitchMode>
          {mode === 'login' ? (
            <>{t('auth.noAccount').split('?')[0]}? <span onClick={() => {
              setMode('register');
              setError('');
              setFormData({ firstName: '', lastName: '', dateOfBirth: '', phoneNumber: '', sex: 'MALE', email: '', password: '', specialization: '', professionalDescription: '', stateCertificateNumber: '' });
            }}>{t('auth.noAccount').split('? ')[1] || 'Register'}</span></>
          ) : (
            <>{t('auth.hasAccount').split('?')[0]}? <span onClick={() => {
              setMode('login');
              setError('');
              setFormData({ firstName: '', lastName: '', dateOfBirth: '', phoneNumber: '', sex: 'MALE', email: '', password: '', specialization: '', professionalDescription: '', stateCertificateNumber: '' });
            }}>{t('auth.hasAccount').split('? ')[1] || 'Log in'}</span></>
          )}
        </SwitchMode>
      </GlassCard>
      
      <Modal 
        isOpen={modalConfig.isOpen} 
        onClose={() => setModalConfig({ ...modalConfig, isOpen: false })}
        title={modalConfig.title}
        message={modalConfig.message}
        type={modalConfig.type}
      />
    </AuthContainer>
  );
};

export default AuthPage;
