import Link from 'next/link'

import Layout from '../components/Layout'
import styled from 'styled-components'
import { Calender, Back, Vacation } from '@navikt/ds-icons'
import { Knapp } from 'nav-frontend-knapper'
import NavInfoCircle from '../components/NavInfoCircle'

const IncidentsContainer = styled.div`
    margin: 20px 0;
    width: 100%;
`
const CenterContent = styled.div`
    margin: 0 auto;
    max-width: 1100px;
    padding: 0 1.5rem;
`

const SectionContainer = styled.div`
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: flex-start;
    margin: 10px 0;
`
const IconWrapper = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    *:first-child{
        margin-right: 20px;
    }
`

const IncidentsWrapper = styled.div`
    padding: 30px 0;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    
`
const ExistsIncidents = styled.div``

const WhiteBackgroundContainer = styled.div`
    background-color: var(--navBakgrunn);
    width: 100%;
    height: 100%;
`

const Incidents = () => {
    const numberOfIncidents: number = 0
    return (
        <Layout>
            <IncidentsContainer>
                <CenterContent>
                    <Link href="/"><Knapp mini><Back/>Gå tilbake</Knapp></Link>



                    <SectionContainer>
                        <IconWrapper>
                            <Calender style={{"fontSize": "3rem"}} aria-label="Kalender ikon" role="img" focusable="false"/>
                            <div>
                                <h3>Hendelser</h3>
                                <span>Siste 48 timer</span>
                            </div>
                        </IconWrapper>
                    </SectionContainer>

                </CenterContent>


                {/* TODO: Handle Incidents within this wrapper.  */}
                <WhiteBackgroundContainer>
                    <CenterContent>
                        <IncidentsWrapper>
                            <CenterContent>
                                {numberOfIncidents > 0 ? (
                                        <ExistsIncidents>
                                            <NavInfoCircle topText={"Antall hendelser"} centerText={numberOfIncidents} bottomText="Siste 48 timene" />
                                        </ExistsIncidents>
                                    ) : (
                                        <CenterContent>
                                            <Vacation/> 
                                            <p>
                                                Ingen hendelser registrert! Hurra!
                                            </p>
                                        </CenterContent>
                                    )
                            }
                            </CenterContent>
                        </IncidentsWrapper>
                    </CenterContent>
                </WhiteBackgroundContainer>


                <CenterContent>
                    <SectionContainer>
                        <IconWrapper>
                            <Calender style={{"fontSize": "3rem"}} aria-label="Kalender ikon" role="img" focusable="false"/>
                            <div>
                                <h3>Hendelser</h3>
                                <span>Siste 90 dagene</span>
                            </div>
                        </IconWrapper>
                    </SectionContainer>
                </CenterContent>
                


                <CenterContent>
                    <IncidentsWrapper>
                        BANNER og OPPSUMMERING siste 90 dager
                    </IncidentsWrapper>
                </CenterContent>



            </IncidentsContainer>
        </Layout>
    )
}

export default Incidents